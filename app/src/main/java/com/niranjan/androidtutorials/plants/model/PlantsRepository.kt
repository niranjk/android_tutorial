package com.niranjan.androidtutorials.plants.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.niranjan.androidtutorials.plants.ComparablePair
import com.niranjan.androidtutorials.plants.db.PlantsDao
import com.niranjan.androidtutorials.plants.network.CacheOnSuccess
import com.niranjan.androidtutorials.plants.network.NetworkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * Repository module for handling data operations.
 *
 * This PlantRepository exposes two UI-observable database queries [plants] and
 * [getPlantsWithGrowZone].
 *
 * To update the plants cache, call [tryUpdateRecentPlantsForGrowZoneCache] or
 * [tryUpdateRecentPlantsCache].
 */
class PlantsRepository private constructor(
    private val plantDao: PlantsDao,
    private val plantService: NetworkService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    /**
     * Custom Sorting a Plant
     * Rules *
     * 1. List Certain Plants First (Orange, Sunflower, Grape, Avocado)
     * 2. Then List the Plants in Alphabetical Order

     * plantsListSortOrderCache is used as the in-memory cache for the custom sort order.
     * It will fallback to an empty list if there's a network error, so that our app
     * can display data even if the sorting order isn't fetched.
     */
    private var plantsListSortOrderCache =
        CacheOnSuccess(onErrorFallback = {
            listOf<String>()
        }){
            plantService.customPlantSortOrder()
        }

    /**
     * Custom Sort Flow :
     * Create a flow, when collected will call getOrWait and emit the sort order.
     */
    private val customSortFlow = flow { emit(plantsListSortOrderCache.getOrAwait()) }

    /**
     * since the flow only emit a singe value : you can build directly from
     * [getOrAwait] and [asFlow]
     */
    private val alternativeCustomSortFlow = plantsListSortOrderCache::getOrAwait.asFlow()

    /**
     * Using Flow in [PlantsRepository]
     */
    val plantsFlow: Flow<List<Plants>>
        get() = plantDao.getPlantsFlow()

    /**
     * Let's combine [plantsFlow] and [customSortFlow] declaratively using transforms [combine]
     *
     * when the result of [customSortFlow] is available, this will combine with the latest value from [plantsFlow]
     *
     * As long as both `plants` and `sortOrder` have initial values (their flow has emitted at least one value)
     * any change in either `plants` and `sortOrder` will call `plants.applySort(order)`
     */

    /**
     * The operator [flowOn] launches a new coroutine to collect the flow and introduces a buffer to write the results.
     *
     * The operator [conflate] will say to store only the most recent result.
     */
    val combinedPlantsFlow : Flow<List<Plants>>
        get() = plantDao.getPlantsFlow()
            .combine(customSortFlow) { plants, sortOrder ->
                plants.applySort(sortOrder)
            }
            .flowOn(defaultDispatcher)
            .conflate()


    /**
     * Fetch a list of [Plants]s from the database.
     * Returns a LiveData-wrapped List of Plants.
     */
    val plants = plantDao.getPlants()

    // apply custom sort order to list
    val plantsCustomSorted : LiveData<List<Plants>> = liveData {
        // Observer plants from the db (just like a normal LiveData + Room return)
        val plantsLiveData = plantDao.getPlants()
        // fetch custom sort from the network in a main-safe suspending call (cached)
        val customSortOrder = plantsListSortOrderCache.getOrAwait()
        // map the LiveData, applying the sort
        emitSource(plantsLiveData.map { plantsList -> plantsList.applySort(customSortOrder) })
    }

    /**
     * You can emit multiple values from a LiveData by calling the emitSource() function
     * whenever you want to emit a new value.
     * Each call to emitSource() removes the previously-added source.
     *
     * Note* If any of the suspend function calls fails, the entire block is canceled and
     * not restarted, which helps to avoid leaks.
     */
    // sorted plants
    val sortedPlants : LiveData<List<Plants>> = liveData {
        val customSortOrder = plantsListSortOrderCache.getOrAwait()
        emitSource(
            plants.map {
                plantsList -> plantsList.applySort(customSortOrder)
            }
        )
    }

    /**
     * [Transformations.map]
     */
    val plantsWithNameStartWithN: LiveData<List<Plants>> = Transformations.map(sortedPlants){ plantList ->
        plantList?.filter { plant ->
            plant.name.startsWith("N")
        }
    }

    /**
     * [Transformations.switchMap]
     */
    val plantsWithNoGrowZone: LiveData<List<Plants>> = Transformations.switchMap(sortedPlants){ plantList ->
        liveData<List<Plants>> {
            plantList?.filter { plants ->
                plants.growZoneNumber == NoGrowZone.number
            }
        }
    }

    /**
     * Fetch a list of [Plants]s from the database that matches a given [GrowZone].
     * Returns a LiveData-wrapped List of Plants.
     */
    fun getPlantsWithGrowZone(growZone: GrowZone) =
        plantDao.getPlantsWithGrowZoneNumber(growZone.number)

    /**
     * Get Plants With Grow Zone Flow
     */

    fun getPlantsWithGrowZoneFlow(growZone: GrowZone) =
        plantDao.getPlantsWithGrowZoneNumberFlow(growZone.number)

    /**
     * Flow supports coroutines.
     *
     * Here we see inside a map operator we can orchestrate multiple async operations
     * without applying any extra transformations.
     */
    fun getPlantsWithGrowZoneFlowCustom(growZone: GrowZone) : Flow<List<Plants>> {
        return plantDao.getPlantsWithGrowZoneNumberFlow(growZone.number).
            // declarative
                map { plantList ->
                    // imperative code
                    val sortOrderFromNetwork = plantsListSortOrderCache.getOrAwait()
                    val nextValue = plantList.applyMainSafeSort(sortOrderFromNetwork)
                    nextValue
                }
    }

    // sort plants with grow zone
    fun getSortedPlantsWithGrowZone(growZone: GrowZone) = liveData<List<Plants>> {
        val plantsGrowZoneLiveData = plantDao.getPlantsWithGrowZoneNumber(growZone.number)
        val customSortOrder = plantsListSortOrderCache.getOrAwait()
        emitSource(plantsGrowZoneLiveData.map {
            plantsList ->
            plantsList.applySort(customSortOrder)
        })
    }


    /**
     * Returns true if we should make a network request.
     */
    private fun shouldUpdatePlantsCache(): Boolean {
        // suspending function, so you can e.g. check the status of the database here
        return true
    }

    /**
     * Update the plants cache.
     *
     * This function may decide to avoid making a network requests on every call based on a
     * cache-invalidation policy.
     */
    suspend fun tryUpdateRecentPlantsCache() {
        if (shouldUpdatePlantsCache()) fetchRecentPlants()
    }

    /**
     * Update the plants cache for a specific grow zone.
     *
     * This function may decide to avoid making a network requests on every call based on a
     * cache-invalidation policy.
     */
    suspend fun tryUpdateRecentPlantsForGrowZoneCache(growZoneNumber: GrowZone) {
        if (shouldUpdatePlantsCache()) fetchPlantsForGrowZone(growZoneNumber)
    }

    /**
     * Fetch a new list of plants from the network, and append them to [plantDao]
     */
    private suspend fun fetchRecentPlants() {
        val plants = plantService.allPlants()
        plantDao.insertAll(plants)
    }

    /**
     * Fetch a list of plants for a growth zone from the network, and append them to [plantDao]
     */
    private suspend fun fetchPlantsForGrowZone(growZone: GrowZone) {
        val plants = plantService.plantsByGrowZone(growZone)
        plantDao.insertAll(plants)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PlantsRepository? = null

        fun getInstance(plantDao: PlantsDao, plantService: NetworkService) =
            instance ?: synchronized(this) {
                instance ?: PlantsRepository(plantDao, plantService).also { instance = it }
            }
    }

    private fun List<Plants>.applySort(customSortOrder: List<String>): List<Plants> {
        return sortedBy { plants ->
            val positionForItem = customSortOrder.indexOf(plants.plantId).let { order ->
                if (order > -1) order else Int.MAX_VALUE
            }
            ComparablePair(positionForItem, plants.name)
        }
    }

    /**
     * Making complex transforms in LiveData
     * switchMap will point to a new LiveData everytime a new value is received
     */
    fun getSortedPlantsWithGrowZoneInSafeMode(growZone: GrowZone) =
        plantDao.getPlantsWithGrowZoneNumber(growZone.number)
            .switchMap { plantList ->
                liveData<List<Plants>> {
                    val customSortOrder = plantsListSortOrderCache.getOrAwait()
                    /***
                     * The result is emitted to the switchMap as the new value is received
                     */
                    emit(
                        plantList.applyMainSafeSort(customSortOrder)
                    )
                }
            }
    private suspend fun List<Plants>.applyMainSafeSort(customSortOrder: List<String>) =
        // switch to dispatcher provided
        withContext(defaultDispatcher) {
            this@applyMainSafeSort.applySort(customSortOrder)
        }
    // return backs to same dispatcher that called it.
}
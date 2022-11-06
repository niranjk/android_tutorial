package com.niranjan.androidtutorials.plants.viewmodel

import androidx.lifecycle.*
import com.niranjan.androidtutorials.plants.model.GrowZone
import com.niranjan.androidtutorials.plants.model.NoGrowZone
import com.niranjan.androidtutorials.plants.model.Plants
import com.niranjan.androidtutorials.plants.model.PlantsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class PlantsViewModel(
    private val plantsRepository: PlantsRepository
) : ViewModel(){
    /**
     * Request a snack-bar to display a string.
     *
     * This variable is private because we don't want to expose [MutableLiveData].
     * MutableLiveData allows anyone to set a value, and [PlantsViewModel] is the only
     * class that should be setting values.
     * @param getValue()
     * @param postValue(value: T)
     * @param setValue(value: T)
     */
    private val _snackbar = MutableLiveData<String?>()

    /**
     * public immutable [LiveData] that allows anyone to observer the values.
     * @param getValue()
     * ViewModel only exposes the immutable [LiveData] object to the observers.
     */
    val snackbar: LiveData<String?>
        get() = _snackbar

    private val _spinner = MutableLiveData<Boolean>(false)
    /**
     * Show a loading spinner if true
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * The current growZone selection.
     */
    private val growZone = MutableLiveData<GrowZone>(NoGrowZone)
    /**
     * A list of plants that updates based on the current filter.
     */
    val plants: LiveData<List<Plants>> = growZone.switchMap { growZone ->
        if (growZone == NoGrowZone) {
            plantsRepository.sortedPlants
        } else {
            plantsRepository.getPlantsWithGrowZone(growZone)
        }
    }

    /**
     * Switching between two flows
     *
     * Let's understand the types of flow
     * 1. flow created by flow{} builder : cold flows which only execute when they're collected.
     * 2. [StateFlow] : created with an initial value and keeps its state same even without being collected and between
     * subsequent collections
     * 3. [MutableStateFlow] : change the value(state) of [StateFlow]
     *
     * [flatMapLatest] is a Flow's extension function that allow us to switch between multiple flows.
     */

    private val growZoneFlow = MutableStateFlow<GrowZone>(NoGrowZone)

    val plantsUsingFlowLiveData: LiveData<List<Plants>> = growZoneFlow.flatMapLatest { growZone ->
        if (growZone == NoGrowZone) plantsRepository.plantsFlow
        else plantsRepository.getPlantsWithGrowZoneFlow(growZone)
    }.asLiveData()

    val plantsUsingFlow = growZoneFlow.flatMapLatest { growZone ->
        if (growZone == NoGrowZone) plantsRepository.plantsFlow
        else plantsRepository.getPlantsWithGrowZoneFlow(growZone)
    }

    /**
     * Using Flow in [ViewModel]
     * Using [LiveData] in UI Layer because it survives configuration changes.
     * We do not need to restart our query everytime the configuration changes.
     * @param asLiveData operator helps to convert [Flow] into a [LiveData] with configurable timeout.
     * This timeout will help the Flow survive restart.
     * If another screen observes before the timeout, the Flow won't be cancelled.
     */
    val plantsUsingFlowConvertingToLiveData : LiveData<List<Plants>> =
        plantsRepository.plantsFlow.asLiveData()

    /**
     * Using [Flow] in UI Layer and collecting it
     * Flow offers main-safety and the ability to cancel, you can choose to pass the Flow all
     * the way through to the UI layer without converting to the LiveData.
     */
    val plantsUsingFlowOnly  = plantsRepository.plantsFlow

    val combinedPlantsFlow = plantsRepository.combinedPlantsFlow

    init {
        // When creating a new ViewModel, clear the growth zone and perform any related udpates
        clearGrowZoneNumber()
        // fetch the full plant list
        launchDataLoad {
            plantsRepository.tryUpdateRecentPlantsCache()
        }
    }

    /**
     * Filter the list to this grow zone.
     *
     * In the starter code version, this will also start a network request. After refactoring,
     * updating the growth zone will automatically kick off a network request.
     */
    fun setGrowZoneNumber(num: Int) {
        // growZone.value = GrowZone(num)
        growZoneFlow.value = GrowZone(num)

        // initial code version, will move during flow rewrite
        launchDataLoad {
            plantsRepository.tryUpdateRecentPlantsCache()
            plantsRepository.tryUpdateRecentPlantsForGrowZoneCache(GrowZone(num))
        }
    }

    /**
     * Clear the current filter of this plants list.
     *
     * In the starter code version, this will also start a network request. After refactoring,
     * updating the growth zone will automatically kick off a network request.
     */
    fun clearGrowZoneNumber() {
        growZone.value = NoGrowZone
        growZoneFlow.value = NoGrowZone

        // initial code version, will move during flow rewrite
        launchDataLoad { plantsRepository.tryUpdateRecentPlantsCache() }
    }

    /**
     * Return true iff the current list is filtered.
     */
    fun isFiltered() = growZone.value != NoGrowZone

    /**
     * Set Value to [MutableLiveData] in ViewModel.
     */
    fun onSnackbarShowWithErrorMessage(error: String?) {
        _snackbar.value = error
    }
    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackbar.value = null
    }

    /**
     * Helper function to call a data load function with a loading spinner; errors will trigger a
     * snack-bar.
     *
     * By marking [block] as [suspend] this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling
     *              the lambda, the loading spinner will display. After completion or error, the
     *              loading spinner will stop.
     */
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable) {
                onSnackbarShowWithErrorMessage(error.message)
                // _snackbar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}
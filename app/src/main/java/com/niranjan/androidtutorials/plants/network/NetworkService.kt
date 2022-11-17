package com.niranjan.androidtutorials.plants.network

import androidx.room.Delete
import com.niranjan.androidtutorials.plants.model.GrowZone
import com.niranjan.androidtutorials.plants.model.Plants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

class NetworkService {
    /**
     * Create a Retrofit Object
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val sunflowerService = retrofit.create(PlantService::class.java)

    suspend fun allPlants(): List<Plants> = withContext(Dispatchers.Default) {
        val result = sunflowerService.getAllPlants()
        result.shuffled()
    }

    suspend fun plantsByGrowZone(growZone: GrowZone) = withContext(Dispatchers.Default) {
        val result = sunflowerService.getAllPlants()
        result.filter { it.growZoneNumber == growZone.number }.shuffled()
    }

    suspend fun customPlantSortOrder(): List<String> = withContext(Dispatchers.Default) {
        val result = sunflowerService.getCustomPlantSortOrder()
        result.map { plant -> plant.plantId }
    }
}

/**
 * First we define our Retrofit Services
 * Here we declare all the [HTTP] Endpoints we use in our App.
 *
 * Note* we can use the coroutines with Retrofit, so we used the
 * [suspend] modifier in the interface methods.
 */
interface PlantService {
    /**
     * [GET] is annotation indicating the type of request
     * and inside we provide the relative path URL
     */
    @GET("googlecodelabs/kotlin-coroutines/master/advanced-coroutines-codelab/sunflower/src/main/assets/plants.json")
    suspend fun getAllPlants() : List<Plants>

    @GET("googlecodelabs/kotlin-coroutines/master/advanced-coroutines-codelab/sunflower/src/main/assets/custom_plant_sort_order.json")
    suspend fun getCustomPlantSortOrder() : List<Plants>
}


interface SamplePlantService {
    @GET("niranjanplantsapi/plants")
    suspend fun getPlantsLists(): List<Plants>
    /**
     * Here we are creating a dynamic URL using [Path]
     * The method argument [id] value gets inserted in the API endpoint
     * niranjanplantsapi/plants/{plantId}.
     * Now the plantId = id
     */
    @GET("niranjanplantsapi/plants/{plantId}")
    suspend fun getPlantById(@Path("plantId") id: String): Plants
    /**
     * Here we append query parameter using the [Query] annotation
     * and the endpoint will become:
     *
     * niranjanplantsapi/plants/search?filter=query
     */
    @GET("niranjanplantsapi/plants/search")
    suspend fun searchPlantsByFilter(@Query("filter") search: String): List<Plants>
    /**
     * You can pass content in a HTTP request body using the [Body] annotation.
     */
    @POST("niranjanplantsapi/plants/new")
    suspend fun createNewPlant(@Body plant: Plants): Plants
    @PUT("niranjanplantsapi/plants/modify")
    suspend fun modifyPlant(@Body plant: Plants): Plants
    @DELETE("niranjanplantsapi/plants/delete/{plantId}")
    suspend fun deletePlantById(@Path("plantId") plantId: String)
}
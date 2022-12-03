package com.niranjan.androidtutorials.foto.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.niranjan.androidtutorials.MainConstants
import com.niranjan.androidtutorials.MainConstants.MY_WORK_A_ID
import com.niranjan.androidtutorials.MainConstants.WORK_REQUEST_OUTPUT_DATA
import com.niranjan.androidtutorials.plants.model.Plants
import com.niranjan.androidtutorials.plants.network.NetworkService

/**
 * This Coroutine Worker class will sync app with the server data.
 *
 * We extend the [CoroutineWorker] class and override the
 * suspending [doWork] method.
 */
class SyncPlantsImagesWorker(
    ctx: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(ctx, workerParameters) {
    override suspend fun doWork(): Result {
        // do work here
        val appContext = applicationContext
        // Access your Input Data Object
        val myData = inputData.getString(MainConstants.WORK_REQUEST_INPUT_DATA_KEY)
        val plantService = NetworkService()
        makeStatusNotification(
            "Syncing App Pictures...Started",
            appContext
        )
        return try {
            val totalImagesSynced = syncPlantsFoto(plantService).size
            // Indicate if work finished successfully with the Result
            makeStatusNotification(
                "Synced Pictures: $totalImagesSynced..",
                appContext
            )
            // Return the output data to the WorkManger in Result.success()
            val outputData = workDataOf(
                WORK_REQUEST_OUTPUT_DATA to totalImagesSynced,
                MY_WORK_A_ID to id
            )
            return Result.success(outputData)
        } catch (throwable: Throwable){
            Result.failure()
        }
    }
}


private suspend fun syncPlantsFoto(service: NetworkService): List<Plants> =
    service.allPlants().sortedBy { it.plantId }



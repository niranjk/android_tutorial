package com.niranjan.androidtutorials.foto.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.niranjan.androidtutorials.plants.model.Plants
import com.niranjan.androidtutorials.plants.network.NetworkService

/**
 * This Coroutine Worker class will sync app with the server recent images
 */
class SyncPlantsImagesWorker(
    ctx: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(ctx, workerParameters) {

    override suspend fun doWork(): Result {
        // do work here
        val appContext = applicationContext
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
            return Result.success()
        } catch (throwable: Throwable){
            Result.failure()
        }
    }
}


private suspend fun syncPlantsFoto(service: NetworkService): List<Plants> =
    service.allPlants().sortedBy { it.plantId }



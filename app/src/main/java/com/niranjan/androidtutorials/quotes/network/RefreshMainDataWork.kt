package com.niranjan.androidtutorials.quotes.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.niranjan.androidtutorials.quotes.db.getQuotesDatabase
import com.niranjan.androidtutorials.quotes.model.QuoteRefreshError
import com.niranjan.androidtutorials.quotes.model.QuotesRepository

/**
 * Worker job to refresh titles from the network while the app is in the background.
 *
 * WorkManager is a library used to enqueue work that is guaranteed to execute after its constraints
 * are met. It can run work even when the app is in the background, or not running.
 */
class RefreshMainDataWork(context: Context, params: WorkerParameters, private val network: QuotesNetwork) :
    CoroutineWorker(context, params) {

    /**
     * Refresh the title from the network using [QuotesRepository]
     *
     * WorkManager will call this method from a background thread. It may be called even
     * after our app has been terminated by the operating system, in which case [WorkManager] will
     * start just enough to run this [Worker].
     */
    override suspend fun doWork(): Result {
        //Use coroutines from WorkManager
        val database = getQuotesDatabase(applicationContext)
        val repository = QuotesRepository(
            network,
            database.quotesDao
        )
        return try {
            repository.refreshQuotes()
            Result.success()
        } catch (error: QuoteRefreshError){
            Result.failure()
        }
    }

    class Factory(val network: QuotesNetwork = getService()) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
            return RefreshMainDataWork(appContext, workerParameters, network)
        }

    }
}
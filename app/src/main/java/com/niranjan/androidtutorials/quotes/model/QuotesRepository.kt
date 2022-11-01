package com.niranjan.androidtutorials.quotes.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.niranjan.androidtutorials.quotes.db.Quote
import com.niranjan.androidtutorials.quotes.db.QuotesDao
import com.niranjan.androidtutorials.quotes.network.BACKGROUND
import com.niranjan.androidtutorials.quotes.network.QuotesNetwork
import kotlinx.coroutines.*

/**
 * QuotesRepository provides an interface to fetch a quote or request a new one be generated.
 *
 * Repository modules handle data operations. They provide a clean API so that the rest of the app
 * can retrieve this data easily. They know where to get the data from and what API calls to make
 * when data is updated. You can consider repositories to be mediators between different data
 * sources, in our case it mediates between a network API and an offline database cache.
 */
class QuotesRepository(val network: QuotesNetwork, val quotesDao: QuotesDao) {

    /**
     * [LiveData] to load quote.
     *
     * This is the main interface for loading a quote. The quotes will be loaded from the offline
     * cache.
     *
     * Observing this will not cause the quotes to be refreshed, use [QuotesRepository.refreshQuoteWithCallbacks]
     * to refresh the title.
     */
    val quote: LiveData<String?> = quotesDao.quoteLiveData.map { it?.quote }

    /**
     * Refresh the current quote and save the results to the offline cache.
     *
     * This method does not return the new quote. Use [QuotesRepository.quote] to observe
     * the current quote.
     */
    fun refreshQuoteWithCallbacks(titleRefreshCallback: QuoteRefreshCallback) {
        // This request will be run on a background thread by retrofit
        BACKGROUND.submit {
            try {
                // Make network request using a blocking call
                val result = network.fetchQuotesCallback().execute()
                if (result.isSuccessful) {
                    // Save it to database
                    result.body()?.let { Quote(quote = it) }?.let { quotesDao.insertQuoteCallbacks(it) }
                    // Inform the caller the refresh is completed
                    titleRefreshCallback.onCompleted()
                } else {
                    // If it's not successful, inform the callback of the error
                    titleRefreshCallback.onError(
                        QuoteRefreshError("Unable to refresh quote", null))
                }
            } catch (cause: Throwable) {
                // If anything throws an exception, inform the caller
                titleRefreshCallback.onError(
                    QuoteRefreshError("Unable to refresh quote", cause))
            }
        }
    }
    // Add coroutines-based `fun refreshQuotes` here
    /**
     * Refresh the current title and save the results to the offline cache.
     *
     * This method does not return the new title. Use [QuotesRepository.quote] to observe
     * the current tile.
     */
    suspend fun refreshQuotes(){
        try {
            val result = withTimeout(5_000) {
                network.fetchQuotes()
            }
            quotesDao.insertQuote(Quote(quote = result))
        }catch (error: Throwable){
            throw QuoteRefreshError("Unable to refresh quotes", error)
        }
    }

    /**
    suspend fun fetchData() {
        val data = getData("...") // <-- suspend until you get data
        show(data) // <--- resume after you get data
    }
    */

    /***
     * Interacting with *Blocking* network and IO Calls using Coroutines
     */
    suspend fun refreshQuotesWithContext(){
        // Starts on Dispatchers.Main
        withContext(Dispatchers.IO) {
            // switches to Dispatchers.IO just within this lambda and return result of the
            // lambda to the dispatcher.Main where it is started
            val result = try {
                // make network request using a blocking call
                network.fetchQuotesCallback().execute()
            } catch (cause: Throwable){
                // inform the caller when network throws exceptions
                throw QuoteRefreshError("Unable to refresh quotes", cause)
            }

            if (result.isSuccessful){
                // save it to database
                quotesDao.insertQuote(Quote(quote = result.body()!!))
            } else {
                // inform the caller of error
                throw QuoteRefreshError("Unable to refresh quotes", null)
            }
        }
        // Returns to Dispatchers.Main
    }

    /**
     * This API is exposed for callers from the Java Programming language.
     *
     * The request will run unstructured, which means it won't be able to be cancelled.
     *
     * @param quotesRefreshCallback a callback
     */
    fun refreshQuoteInterop(quotesRefreshCallback: QuoteRefreshCallback) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                refreshQuotes()
                quotesRefreshCallback.onCompleted()
            } catch (throwable: Throwable) {
                quotesRefreshCallback.onError(throwable)
            }
        }
    }

}

/**
 * Thrown when there was a error fetching a new quote
 *
 * @property message user ready error message
 * @property cause the original cause of this exception
 */
class QuoteRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)

interface QuoteRefreshCallback {
    fun onCompleted()
    fun onError(cause: Throwable)
}
package com.niranjan.androidtutorials.quotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niranjan.androidtutorials.quotes.model.QuoteRefreshCallback
import com.niranjan.androidtutorials.quotes.model.QuoteRefreshError
import com.niranjan.androidtutorials.quotes.model.QuotesRepository
import com.niranjan.androidtutorials.quotes.network.BACKGROUND
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * QuotesViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param repository the data source this ViewModel will fetch results from.
 */

class QuotesViewModel(
    private val repository: QuotesRepository
) : ViewModel(){
    companion object {
        /**
         * Factory for creating [QuotesViewModel]
         *
         * @param arg the repository to pass to [QuotesViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::QuotesViewModel)
    }

    /**
     * Request a snackbar to display a string.
     *
     * This variable is private because we don't want to expose MutableLiveData
     *
     * MutableLiveData allows anyone to set a value, and MainViewModel is the only
     * class that should be setting values.
     */
    private val _snackBar = MutableLiveData<String?>()

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String?>
        get() = _snackBar

    /**
     * Update title text via this LiveData
     */
    val quote = repository.quote

    private val _spinner = MutableLiveData<Boolean>(false)

    /**
     * Show a loading spinner if true
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Count of taps on the screen
     */
    private var tapCount = 0

    /**
     * LiveData with formatted tap count.
     */
    private val _taps = MutableLiveData<String>("$tapCount taps")

    /**
     * Public view of tap live data.
     */
    val taps: LiveData<String>
        get() = _taps

    /**
     * Respond to onClick events by refreshing the title.
     *
     * The loading spinner will display until a result is returned, and errors will trigger
     * a snackbar.
     */
    fun onMainViewClicked() {
        refreshQuote()
        updateTaps()
    }

    /**
     * Wait one-second then update the tap count.
     */
    private fun updateTapsCallbacks() {
        //Convert updateTaps to use coroutines
        tapCount++
        BACKGROUND.submit {
            Thread.sleep(1_000)
            _taps.postValue("${tapCount} taps")
        }
    }

    private fun updateTaps(){
        viewModelScope.launch {
            delay(1_000)
            _taps.value = "${++tapCount} taps"
        }
    }

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the title, showing a loading spinner while it refreshes and errors via snackbar.
     */
    fun refreshQuoteCallbacks() {
        // Convert refreshQuote to use coroutines
        _spinner.value = true
        repository.refreshQuoteWithCallbacks(object : QuoteRefreshCallback {
            // when the result is ready this callback will get the result
            override fun onCompleted() {
                _spinner.postValue(false)
            }
            override fun onError(cause: Throwable) {
                _snackBar.postValue(cause.message)
                _spinner.postValue(false)
            }
        })
    }

    fun refreshQuote()= launchDataLoad{
        repository.refreshQuotes()
    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    private fun launchDataLoad(block: suspend () -> Unit): Unit {
        viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: QuoteRefreshError){
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}
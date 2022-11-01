package com.niranjan.androidtutorials.plants.viewmodel

import androidx.lifecycle.*
import com.niranjan.androidtutorials.plants.model.GrowZone
import com.niranjan.androidtutorials.plants.model.NoGrowZone
import com.niranjan.androidtutorials.plants.model.Plants
import com.niranjan.androidtutorials.plants.model.PlantsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlantsViewModel(
    private val plantsRepository: PlantsRepository
) : ViewModel(){
    /**
     * Request a snack-bar to display a string.
     *
     * This variable is private because we don't want to expose [MutableLiveData].
     *
     * MutableLiveData allows anyone to set a value, and [PlantsViewModel] is the only
     * class that should be setting values.
     */
    private val _snackbar = MutableLiveData<String?>()

    /**
     * Request a snack-bar to display a string.
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
            plantsRepository.plants
        } else {
            plantsRepository.getPlantsWithGrowZone(growZone)
        }
    }

    // todo use flow


    init {
        // When creating a new ViewModel, clear the growth zone and perform any related udpates
        clearGrowZoneNumber()
    }

    /**
     * Filter the list to this grow zone.
     *
     * In the starter code version, this will also start a network request. After refactoring,
     * updating the growth zone will automatically kick off a network request.
     */
    fun setGrowZoneNumber(num: Int) {
        growZone.value = GrowZone(num)

        // initial code version, will move during flow rewrite
        launchDataLoad { plantsRepository.tryUpdateRecentPlantsCache() }
    }

    /**
     * Clear the current filter of this plants list.
     *
     * In the starter code version, this will also start a network request. After refactoring,
     * updating the growth zone will automatically kick off a network request.
     */
    fun clearGrowZoneNumber() {
        growZone.value = NoGrowZone

        // initial code version, will move during flow rewrite
        launchDataLoad { plantsRepository.tryUpdateRecentPlantsCache() }
    }

    /**
     * Return true iff the current list is filtered.
     */
    fun isFiltered() = growZone.value != NoGrowZone

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
                _snackbar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}
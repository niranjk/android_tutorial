package com.niranjan.androidtutorials.plants.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.niranjan.androidtutorials.databinding.FragmentPlantsBinding
import com.niranjan.androidtutorials.plants.model.PlantsRepository
import com.niranjan.androidtutorials.plants.viewmodel.Injector
import com.niranjan.androidtutorials.plants.viewmodel.PlantsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlantsFragment : Fragment() {
    private val viewModel: PlantsViewModel by viewModels {
        Injector.providePlantsViewModelFactory(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlantsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner) { show ->
            binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
        }

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }

        val adapter = PlantsAdapter()
        binding.plantList.adapter = adapter
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * 1st Approach : Using LiveData Only
     *
     * 2nd Approach : Using Flow converted to LiveData
     *
     * 3rd Approach : Using Flow Only all the way through the UI
     */
    private fun subscribeUi(adapter: PlantsAdapter) {
        /*
        // 1st Approach
        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
        }
        // 2nd Approach
        viewModel.plantsUsingFlowConvertingToLiveData.observe(viewLifecycleOwner){ plants ->
            adapter.submitList(plants)
        }
         */
        // 3rd Approach
        lifecycleScope.launch {
            viewModel.plantsUsingFlowOnly.collect{ plantsList ->
                adapter.submitList(plantsList)
            }
        }
    }

    private fun updateData() {
        with(viewModel) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(9)
            }
        }
    }
}

/**
 * Factory for creating a [PlantListViewModel] with a constructor that takes a [PlantRepository].
 *
 * The @ExperimentalCoroutinesApi and @FlowPreview indicate that experimental APIs are being used.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class PlantsViewModelFactory(
    private val repository: PlantsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PlantsViewModel(repository) as T
}
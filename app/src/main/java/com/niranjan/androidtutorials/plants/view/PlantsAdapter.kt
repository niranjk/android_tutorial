package com.niranjan.androidtutorials.plants.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.niranjan.androidtutorials.databinding.ListItemPlantBinding
import com.niranjan.androidtutorials.plants.model.Plants

/**
 * Custom Adapter build extending the ListAdapter
 * 1. Add the Data Type
 * 2. Add the DiffUtil.ItemCallback Object to differentiate two lists
 */
class PlantsAdapter : ListAdapter<Plants, PlantsAdapter.PlantViewHolder>(PlantDiffCallback()) {
    /**
     * If your BindingAdapter does time-consuming processes or image-loading code
     * we have to call*
     * It also ensuere that the framework does everything it needs to properly calculate
     * space for the item since changing the binding variable is not always immediate.
     * @param executePendingBindings
     */
    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val item = getItem(position)
        // apply the binding here
        holder.binding.apply {
            /*  plant is a variable we declare in the xml file
                this will set values on the View automatically   */
            plant = item
            executePendingBindings()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        return PlantViewHolder.from(parent)
    }
    /**
     * We pass binding as a paramter to ViewHolder to set up binding adapters and tie it to the view.
     */
    class PlantViewHolder(
        val binding: ListItemPlantBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        /**
         * If we want a function or a property to be tied to a class rather than to
         * instances of it, we declare it as a companion object.
         */
        companion object {
            fun from(parent: ViewGroup): PlantViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPlantBinding.inflate(layoutInflater, parent, false)
                return PlantViewHolder(binding) // setup binding
            }
        }
    }
}

/**
 * DiffUtil.ItemCallback calculates the difference between two non-null items in a list.
 *
 * It overrides 2 methods
 *
 * [areItemsTheSame]   : checking the object based on ID
 *
 * [areContentsTheSame] : checking if the objects are visually same
 */
private class PlantDiffCallback : DiffUtil.ItemCallback<Plants>() {

    override fun areItemsTheSame(oldItem: Plants, newItem: Plants): Boolean {
        return oldItem.plantId == newItem.plantId
    }

    override fun areContentsTheSame(oldItem: Plants, newItem: Plants): Boolean {
        return oldItem == newItem
    }
}
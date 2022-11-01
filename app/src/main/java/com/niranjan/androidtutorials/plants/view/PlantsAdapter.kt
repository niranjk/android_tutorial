package com.niranjan.androidtutorials.plants.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.niranjan.androidtutorials.databinding.ListItemPlantBinding
import com.niranjan.androidtutorials.plants.model.Plants

class PlantsAdapter : ListAdapter<Plants, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            ListItemPlantBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    class PlantViewHolder(
        private val binding: ListItemPlantBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Plants) {
            binding.apply {
                plant = item
                executePendingBindings()
            }
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<Plants>() {

    override fun areItemsTheSame(oldItem: Plants, newItem: Plants): Boolean {
        return oldItem.plantId == newItem.plantId
    }

    override fun areContentsTheSame(oldItem: Plants, newItem: Plants): Boolean {
        return oldItem == newItem
    }
}
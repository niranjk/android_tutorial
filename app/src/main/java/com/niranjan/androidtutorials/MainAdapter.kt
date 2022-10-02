package com.niranjan.androidtutorials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.niranjan.androidtutorials.databinding.RecyclerItemViewBinding

class MainAdapter(
    private val itemList: List<MainItemUiModel>,
    val itemListeners : (title: String) -> Unit // item click listener
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>(){

    private lateinit var binding : RecyclerItemViewBinding
    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        binding = RecyclerItemViewBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class MainViewHolder(binding: RecyclerItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainItemUiModel){
            binding.itemTv.text = item.title
            binding.itemContainer.setOnClickListener {
                itemListeners.invoke(item.title)
            }
        }
    }
}
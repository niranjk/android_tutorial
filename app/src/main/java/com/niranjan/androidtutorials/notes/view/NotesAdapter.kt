package com.niranjan.androidtutorials.notes.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.niranjan.androidtutorials.databinding.RecyclerItemViewBinding
import com.niranjan.androidtutorials.notes.db.NotesEntity

class NotesAdapter :
    androidx.recyclerview.widget.ListAdapter<NotesEntity, NotesAdapter.NotesViewHolder>(WORDS_COMPARATOR){

    private lateinit var binding: RecyclerItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        binding = RecyclerItemViewBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotesViewHolder(binding: RecyclerItemViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: NotesEntity) {
            binding.itemTv.text = item.note
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<NotesEntity>() {
            override fun areItemsTheSame(oldItem: NotesEntity, newItem: NotesEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NotesEntity, newItem: NotesEntity): Boolean {
                return oldItem.note == newItem.note
            }
        }
    }
}
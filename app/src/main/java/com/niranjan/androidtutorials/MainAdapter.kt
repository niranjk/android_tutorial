package com.niranjan.androidtutorials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.niranjan.androidtutorials.databinding.ListItemTopicBinding
import com.niranjan.androidtutorials.databinding.RecyclerItemViewBinding
import com.niranjan.androidtutorials.uimodel.GenericUiModel
import com.niranjan.androidtutorials.uimodel.MainItemUiModel
import com.niranjan.androidtutorials.uimodel.TopicItemUiModel

/**
 * Custom Adapter with Multiple Item View Type
 * 1. Declare different ViewHolder
 * 2. Determine which ViewHolder to bind.
 * @param getItemViewType helps to determine the ViewType of the Item
 */
class MainAdapter(
    private val itemList: List<GenericUiModel>,
    val itemListeners : (title: String) -> Unit // item click listener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int = itemList.size
    /**
     * Override [getItemViewType] to return the appropriate ViewType based on the UiModel
     * return as a ordinal Int value
     */
    override fun getItemViewType(position: Int): Int {
        return when(itemList.get(position)){
            is MainItemUiModel -> MainConstants.MainViewType.VIEW_TYPE_APP_TITLE.ordinal
            else -> MainConstants.MainViewType.VIEW_TYPE_TOPICS.ordinal
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MainConstants.MainViewType.VIEW_TYPE_APP_TITLE.ordinal -> AppTitleViewHolder.from(parent)
            else -> TopicViewHolder.from(parent)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AppTitleViewHolder -> {
                with(holder.binding){
                    itemTv.text = (itemList[position] as MainItemUiModel).title
                    executePendingBindings()
                }
            }
            is TopicViewHolder -> {
                with(holder.binding){
                    topicsItemTv.text = (itemList[position] as TopicItemUiModel).topics
                    executePendingBindings()
                }
            }
        }
    }
    /**
     * Create ViewHolders for each ViewType
     */
    class AppTitleViewHolder(val binding: RecyclerItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): AppTitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemViewBinding.inflate(layoutInflater, parent, false)
                return AppTitleViewHolder(binding)
            }
        }
    }
    class TopicViewHolder(val binding: ListItemTopicBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): TopicViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTopicBinding.inflate(layoutInflater, parent, false)
                return TopicViewHolder(binding)
            }
        }
    }
}
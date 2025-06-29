package com.dev.storeapp.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dev.storeapp.R
import com.dev.storeapp.data.model.MainCategory
import com.dev.storeapp.data.model.CategoryDestination

class MainCategoryAdapter(
    private val onCategoryClick: (CategoryDestination) -> Unit
) : ListAdapter<MainCategory, MainCategoryAdapter.ViewHolder>(CategoryDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.categoryIcon)
        private val title: TextView = itemView.findViewById(R.id.categoryTitle)

        fun bind(category: MainCategory) {
            title.text = category.title
            icon.setImageResource(category.iconRes)

            itemView.setOnClickListener {
                onCategoryClick(category.destination)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_category_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("MainCategoryAdapter", "Binding item: ${item.title}")
        holder.bind(item)
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<MainCategory>() {
        override fun areItemsTheSame(oldItem: MainCategory, newItem: MainCategory): Boolean {
            return oldItem.destination == newItem.destination
        }

        override fun areContentsTheSame(oldItem: MainCategory, newItem: MainCategory): Boolean {
            return oldItem == newItem
        }
    }
}

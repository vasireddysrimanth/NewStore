package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.storeapp.R
import com.dev.storeapp.data.model.TopPicksDestination

class LeftMenuAdapter(
    private var items: List<TopPicksDestination>,
    private val onFeatureClick: (TopPicksDestination) -> Unit
) : RecyclerView.Adapter<LeftMenuAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.leftMenuIcon)
        val title: TextView = view.findViewById(R.id.leftMenuTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_left_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feature = items[position]
        holder.title.text = feature.title
        holder.icon.setImageResource(feature.iconRes)

        holder.itemView.setOnClickListener {
            onFeatureClick(feature)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<TopPicksDestination>) {
        items = list
        notifyDataSetChanged()
    }
}
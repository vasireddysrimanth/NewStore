package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.storeapp.R
import com.dev.storeapp.data.model.AccountFeature
import com.dev.storeapp.data.model.FeatureDestination
import com.google.android.material.button.MaterialButton

class AccountFeaturesAdapter(
    private var items: List<AccountFeature>,
    private val onFeatureClick: (FeatureDestination) -> Unit
) : RecyclerView.Adapter<AccountFeaturesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: MaterialButton = view.findViewById(R.id.cartsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_your_account, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feature = items[position]
        holder.button.text = feature.title
        holder.button.setOnClickListener {
            onFeatureClick(feature.destination)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list:List<AccountFeature>){
        items = list
    }
}

package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.databinding.ItemCartsBinding

class CartsAdapter :RecyclerView.Adapter<CartsAdapter.MyViewHolder>() {

    private val diffCallback =object  : DiffUtil.ItemCallback<AddToCartEntity>(){
        override fun areItemsTheSame(oldItem: AddToCartEntity, newItem: AddToCartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AddToCartEntity, newItem: AddToCartEntity): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartsAdapter.MyViewHolder {
        val binding = ItemCartsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartsAdapter.MyViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    inner class MyViewHolder(private val binding: ItemCartsBinding) : RecyclerView.ViewHolder(binding.root) {

        // Define your view holder here
        fun setData(product: AddToCartEntity){
            binding.apply {
                productName.text = product.title
                priceTextView.text = product.price.toString()
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(com.dev.storeapp.R.drawable.ic_launcher_foreground)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
                root.setOnClickListener {
                    itemClickListener(product)
                }
            }

        }
    }

    private  var itemClickListener: (AddToCartEntity) -> Unit = {}
    fun setOnItemClickListener(listener: (AddToCartEntity) -> Unit) {
        itemClickListener = listener
    }
}
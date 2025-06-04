package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.databinding.ItemProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val diffCallback =object  :DiffUtil.ItemCallback<ProductEntity>(){
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }
    }

     val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        val binding =ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        // Define your view holder here
        fun setData(product: ProductEntity){
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


    private var itemClickListener :((ProductEntity) ->Unit) = {}
    fun setOnItemClickListener(listener :((ProductEntity) ->Unit)){
                itemClickListener = listener
    }
}
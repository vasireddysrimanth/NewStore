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
                productTitle.text = product.title
                productBrand.text = product.brand
                productColor.text = product.color
                productBrand.text = product.brand
                productModel.text = product.model
                productDiscount.text = "${product.discount} % off"
                productPrice.text = product.price.toString()
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(com.dev.storeapp.R.mipmap.store_round)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(productImageView)
                root.setOnClickListener {
                    itemClickListener(product)
                }
                addToCartButton.setOnClickListener {
                    onAddToCartClick?.invoke(product)
                }
            }

        }
    }


    private var itemClickListener :((ProductEntity) ->Unit) = {}
    fun setOnItemClickListener(listener :((ProductEntity) ->Unit)){
                itemClickListener = listener
    }

    private var onAddToCartClick: ((ProductEntity) -> Unit)? = null
    fun setOnAddToCartClickListener(listener: (ProductEntity) -> Unit) {
        onAddToCartClick = listener
    }
}
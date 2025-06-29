package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.databinding.ItemCartsBinding

class CartsAdapter : RecyclerView.Adapter<CartsAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<AddToCartEntity>() {
        override fun areItemsTheSame(oldItem: AddToCartEntity, newItem: AddToCartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AddToCartEntity, newItem: AddToCartEntity): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartsAdapter.MyViewHolder {
        val binding = ItemCartsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartsAdapter.MyViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val binding: ItemCartsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(product: AddToCartEntity) {
            binding.apply {
                // Set product details
                txtTitle.text = product.title
                txtColorModel.text = "${product.color} | ${product.model}"
                txtBrand.text = "Brand: ${product.brand}"
                txtDiscount.text = "${product.discount}% off"
                txtPrice.text = product.price.toString()

                // Load product image
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(com.dev.storeapp.R.drawable.ic_launcher_foreground)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProduct)

                // Handle quantity display and controls
                setupQuantityControls(product)

                // Set click listeners
                root.setOnClickListener {
                    itemClickListener(product)
                }

                btnDelete.setOnClickListener {
                    onDeleteItemClickListener?.invoke(product)
                }
            }
        }

        private fun setupQuantityControls(product: AddToCartEntity) {
            binding.apply {
                val quantity = product.quantity ?: 1
                txtQuantity.text = quantity.toString()
                // Handle decrease button state and click
                btnDecrease.isEnabled = quantity > 1
                btnDecrease.alpha = if (quantity > 1) 1.0f else 0.5f

                btnDecrease.setOnClickListener {
                    if (quantity > 1) {
                        val newQuantity = quantity - 1
                        onQuantityChangeListener?.invoke(product, newQuantity)
                    }
                }

                // Handle increase button click
                btnIncrease.setOnClickListener {
                    val newQuantity = quantity + 1
                    onQuantityChangeListener?.invoke(product, newQuantity)
                }

                // Show/hide quantity controls based on quantity
                if (quantity <= 0) {
                    quantityContainer.visibility = View.GONE
                } else {
                    quantityContainer.visibility = View.VISIBLE
                }
            }
        }
    }

    // Click listeners
    private var itemClickListener: (AddToCartEntity) -> Unit = {}
    fun setOnItemClickListener(listener: (AddToCartEntity) -> Unit) {
        itemClickListener = listener
    }

    private var onDeleteItemClickListener: ((AddToCartEntity) -> Unit)? = null
    fun setOnDeleteItemClickListener(listener: (AddToCartEntity) -> Unit) {
        onDeleteItemClickListener = listener
    }

    private var onQuantityChangeListener: ((AddToCartEntity, Int) -> Unit)? = null
    fun setOnQuantityChangeListener(listener: (AddToCartEntity, Int) -> Unit) {
        onQuantityChangeListener = listener
    }
}
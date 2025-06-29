package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.storeapp.R
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.databinding.ItemCheckoutBinding

class CheckOutAdapter(
    private val onQuantityChanged: (List<Product>) -> Unit
) : RecyclerView.Adapter<CheckOutAdapter.MyViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position, differ.currentList.toMutableList(), onQuantityChanged)
    }

    override fun getItemCount() = differ.currentList.size

    class MyViewHolder(private val binding: ItemCheckoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: Product,
            position: Int,
            currentList: MutableList<Product>,
            onQuantityChanged: (List<Product>) -> Unit
        ) {
            binding.apply {
                itemName.text = product.title
                txtQuantity.text = product.quantity.toString()
                updateTotalPrice(product.price, product.quantity)

                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.drawable.user_bg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemImage)

                btnDecrease.apply {
                    isEnabled = product.quantity > 1
                    alpha = if (isEnabled) 1.0f else 0.5f
                    setOnClickListener { updateQuantity(product, position, currentList, onQuantityChanged, -1) }
                }

                btnIncrease.setOnClickListener { updateQuantity(product, position, currentList, onQuantityChanged, 1) }

                quantityContainer.visibility = if (product.quantity > 0) View.VISIBLE else View.GONE
            }
        }

        private fun updateQuantity(
            product: Product,
            position: Int,
            currentList: MutableList<Product>,
            onQuantityChanged: (List<Product>) -> Unit,
            change: Int
        ) {
            val newQuantity = product.quantity + change
            binding.apply {
                txtQuantity.text = newQuantity.toString()
                updateTotalPrice(product.price, newQuantity)
                btnDecrease.apply {
                    isEnabled = newQuantity > 1
                    alpha = if (isEnabled) 1.0f else 0.5f
                }
            }
            currentList[position] = product.copy(quantity = newQuantity)
            onQuantityChanged(currentList)
        }

        private fun updateTotalPrice(pricePerItem: Double, quantity: Int) {
            binding.itemPrice.text = "₹%.2f".format(pricePerItem * quantity)
        }
    }
}
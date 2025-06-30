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

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.setData(product, position, differ.currentList.toMutableList(), onQuantityChanged)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class MyViewHolder(private val binding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(
            product: Product,
            position: Int,
            currentList: MutableList<Product>,
            onQuantityChanged: (List<Product>) -> Unit
        ) {
            binding.apply {
                itemName.text = product.title

                var quantity = product.quantity
                txtQuantity.text = quantity.toString()
                updateTotalPrice(product.price, quantity)

                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.drawable.user_bg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemImage)

                btnDecrease.isEnabled = quantity > 1
                btnDecrease.alpha = if (quantity > 1) 1.0f else 0.5f

                btnDecrease.setOnClickListener {
                    if (quantity > 1) {
                        quantity--
                        txtQuantity.text = quantity.toString()
                        updateTotalPrice(product.price, quantity)
                        btnDecrease.isEnabled = quantity > 1
                        btnDecrease.alpha = if (quantity > 1) 1.0f else 0.5f

                        currentList[position] = product.copy(quantity = quantity)
                        onQuantityChanged(currentList)
                    }
                }

                btnIncrease.setOnClickListener {
                    quantity++
                    txtQuantity.text = quantity.toString()
                    updateTotalPrice(product.price, quantity)
                    btnDecrease.isEnabled = quantity > 1
                    btnDecrease.alpha = if (quantity > 1) 1.0f else 0.5f

                    currentList[position] = product.copy(quantity = quantity)
                    onQuantityChanged(currentList)
                }

                quantityContainer.visibility = if (quantity > 0) View.VISIBLE else View.GONE
            }
        }

        private fun updateTotalPrice(pricePerItem: Double, quantity: Int) {
            val totalPrice = pricePerItem * quantity
            binding.itemPrice.text = "₹%.2f".format(totalPrice)
        }
    }
}

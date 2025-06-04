package com.dev.storeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.storeapp.R
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.databinding.ItemCheckoutBinding

class CheckOutAdapter :RecyclerView.Adapter<CheckOutAdapter.MyViewHolder>() {

    private val diffCallback =object  : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class MyViewHolder(private val binding :ItemCheckoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(product : Product){
            binding.apply {
                itemName.text = product.title
                itemQuantity.text = product.quantity.toString()  // use actual quantity
                val totalPrice = product.price * product.quantity
                itemPrice.text = "₹%.2f".format(totalPrice)      // show total price formatted
                Glide.with(itemView.context)
                    .load(product.image)
                    .placeholder(R.drawable.user_bg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemImage)
            }
        }
    }
}
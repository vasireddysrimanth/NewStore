package com.dev.storeapp.presentation.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.databinding.ItemOrderBinding

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<OrderEntity>() {
        override fun areItemsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MyViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(order: OrderEntity) {
            binding.apply {
                textOrderId.text = "Order ID: #${order.orderId}"
                textPaymentMode.text = "Payment: ${order.paymentMode}"
                textTotalAmount.text = "Total: ₹${order.totalAmount}"
                textOrderTime.text = "Placed on: ${order.timestamp}"

                orderItemsContainer.removeAllViews()
                order.products.forEach { product ->
                    val itemText = TextView(itemView.context).apply {
                        text = "- ${product.title} (x${product.quantity})"
                        setTextColor(Color.DKGRAY)
                        textSize = 14f
                        setPadding(0, 4, 0, 4)
                    }
                    orderItemsContainer.addView(itemText)
                }

                root.setOnClickListener {
                    itemClickListener(order)
                }
            }
        }
    }

    private var itemClickListener: (OrderEntity) -> Unit = {}
    fun setOnItemClickListener(listener: (OrderEntity) -> Unit) {
        itemClickListener = listener
    }
}

package com.dev.storeapp.presentation.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setData(differ.currentList[position])

    }

    override fun getItemCount() = differ.currentList.size

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(user: UserEntity) {
            binding.apply {
                firstName.text = user.name.firstname
                lastName.text = user.name.lastname
                email.text = user.email

                root.setOnClickListener {
                    itemClickListner(user)
                }
            }
        }
    }

    private var itemClickListner: ((UserEntity) -> Unit) = {}
    fun setOnItemClickListener(listener: (UserEntity) -> Unit){
        itemClickListner = listener
    }


}
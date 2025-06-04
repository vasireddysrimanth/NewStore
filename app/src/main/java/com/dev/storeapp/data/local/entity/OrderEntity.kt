package com.dev.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.model.Product

@Entity(DBConstants.ORDER_ENTITY_TABLE)
data class OrderEntity(
    @PrimaryKey
    val orderId : String ,
    val products : List<Product> = emptyList(),
    val totalAmount: Double? = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMode: String? = null
    )
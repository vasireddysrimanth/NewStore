package com.dev.storeapp.domain.repository

import com.dev.storeapp.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun insertOrder(order: OrderEntity)

    suspend fun getAllOrders(): Flow<List<OrderEntity>>

    suspend fun getOrderById(orderId: String): Flow<OrderEntity>?

    suspend fun deleteOrder(order: OrderEntity)
}
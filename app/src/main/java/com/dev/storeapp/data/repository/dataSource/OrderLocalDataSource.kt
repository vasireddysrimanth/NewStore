package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

interface OrderLocalDataSource {

    suspend fun insertOrder(order: OrderEntity)

    fun getAllOrders(): Flow<List<OrderEntity>>

    fun getOrderById(orderId: String): Flow<OrderEntity>?

    suspend fun deleteOrder(order: OrderEntity)

}
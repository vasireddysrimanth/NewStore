package com.dev.storeapp.data.repository

import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.data.repository.dataSource.OrderLocalDataSource
import com.dev.storeapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl  @Inject constructor(
    private val orderLocalDataSource: OrderLocalDataSource
) :OrderRepository{
    override suspend fun insertOrder(order: OrderEntity) = orderLocalDataSource.insertOrder(order)

    override suspend fun getAllOrders(): Flow<List<OrderEntity>>  =
        orderLocalDataSource.getAllOrders()

    override suspend fun getOrderById(orderId: String): Flow<OrderEntity>? = orderLocalDataSource.getOrderById(orderId)

    override suspend fun deleteOrder(order: OrderEntity) = orderLocalDataSource.deleteOrder(order)
}
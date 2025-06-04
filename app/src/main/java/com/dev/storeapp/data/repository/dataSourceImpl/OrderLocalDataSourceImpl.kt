package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.dao.OrderDao
import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.data.repository.dataSource.OrderLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderLocalDataSourceImpl @Inject constructor(
    private val orderDao: OrderDao
) :OrderLocalDataSource {
    override suspend fun insertOrder(order: OrderEntity) {
        orderDao.insertOrder(order)
    }

    override fun getAllOrders(): Flow<List<OrderEntity>> {
        return  orderDao.getAllOrders()
    }

    override fun getOrderById(orderId: String): Flow<OrderEntity>? {
       return orderDao.getOrderById(orderId)
    }

    override suspend fun deleteOrder(order: OrderEntity) {
        orderDao.deleteOrder(order)
    }

}
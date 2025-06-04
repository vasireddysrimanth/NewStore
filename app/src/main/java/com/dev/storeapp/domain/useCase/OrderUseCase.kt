package com.dev.storeapp.domain.useCase

import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.domain.repository.OrderRepository
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend fun getAllOrders() = orderRepository.getAllOrders()
    suspend fun getOrderById(orderId: String) = orderRepository.getOrderById(orderId)
    suspend fun insertOrder(order: OrderEntity) = orderRepository.insertOrder(order)
    suspend fun deleteOrder(order: OrderEntity) = orderRepository.deleteOrder(order)
}
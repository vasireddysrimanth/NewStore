package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.AddToCartEntity
import kotlinx.coroutines.flow.Flow

interface AddToCartLocalDataSource {

    fun getAllCarts(): Flow<List<AddToCartEntity>>

    suspend fun insertToCart(addToCartEntity: AddToCartEntity)

    suspend fun deleteAllCarts()

    suspend fun deleteCartById(id: Int)

    suspend fun isProductInCart(productId: Int): Boolean
}
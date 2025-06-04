package com.dev.storeapp.domain.repository

import com.dev.storeapp.data.local.entity.AddToCartEntity
import kotlinx.coroutines.flow.Flow

interface AddToCartRepository {
    suspend fun insertToCart(addToCartEntity: AddToCartEntity)

    suspend fun getAllCarts(): Flow<List<AddToCartEntity>>

    suspend fun deleteAllCarts()

    suspend fun deleteCartById(id: Int)

    suspend fun isProductInCart(productId: Int): Boolean
}
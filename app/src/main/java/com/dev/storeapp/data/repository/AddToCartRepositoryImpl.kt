package com.dev.storeapp.data.repository

import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.data.repository.dataSource.AddToCartLocalDataSource
import com.dev.storeapp.domain.repository.AddToCartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartRepositoryImpl @Inject constructor(
     private val addToCartLocalDataSource: AddToCartLocalDataSource
) :AddToCartRepository{
    override suspend fun insertToCart(addToCartEntity: AddToCartEntity) {
        addToCartLocalDataSource.insertToCart(addToCartEntity)
    }

    override suspend fun upsertToCart(addToCartEntity: AddToCartEntity) = addToCartLocalDataSource.upsertToCart(addToCartEntity)

    override suspend fun getAllCarts(): Flow<List<AddToCartEntity>> {
        return addToCartLocalDataSource.getAllCarts()
    }

    override suspend fun deleteAllCarts() {
        addToCartLocalDataSource.deleteAllCarts()
    }

    override suspend fun deleteCartById(id: Int) {
        addToCartLocalDataSource.deleteCartById(id)
    }

    override suspend fun isProductInCart(productId: Int) = addToCartLocalDataSource.isProductInCart(productId)
}
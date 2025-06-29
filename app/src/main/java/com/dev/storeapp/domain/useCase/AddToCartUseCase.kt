package com.dev.storeapp.domain.useCase

import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.domain.repository.AddToCartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val addToCartRepository: AddToCartRepository
){

    suspend fun insertToCart(addToCartEntity: AddToCartEntity) {
        addToCartRepository.insertToCart(addToCartEntity)
    }

    suspend fun getAllCarts() = addToCartRepository.getAllCarts()

    suspend fun upsertToCart(addToCartEntity: AddToCartEntity) = addToCartRepository.upsertToCart(addToCartEntity)

    suspend fun deleteAllCarts() = addToCartRepository.deleteAllCarts()

    suspend fun deleteCartById(id: Int) = addToCartRepository.deleteCartById(id)

    suspend fun isProductInCart(productId: Int) = addToCartRepository.isProductInCart(productId)
}
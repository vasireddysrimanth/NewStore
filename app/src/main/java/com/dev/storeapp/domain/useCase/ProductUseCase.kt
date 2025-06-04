package com.dev.storeapp.domain.useCase

import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductUseCase  @Inject constructor(
    private val repository: ProductRepository
){

    suspend fun insertProduct(productEntity: ProductEntity) = repository.insertProduct(productEntity)

    suspend fun deleteProduct(productEntity: ProductEntity) = repository.deleteProduct(productEntity)

    suspend fun deleteProductById(id: Int) = repository.deleteProductById(id)

    suspend fun getAllProducts(): Flow<List<ProductEntity>> = repository.getAllProducts()

    suspend fun getProductById(id: Int): Flow<ProductEntity> = repository.getProductById(id)

    suspend fun sync(): Boolean = repository.sync()

    suspend fun createProductToServer(productEntity: ProductEntity) = repository.createProductToServer(productEntity)

}
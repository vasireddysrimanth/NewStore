package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.ProductEntity

interface ProductRemoteDataSource {
    suspend fun getRemoteProducts(): List<ProductEntity>
    suspend fun getRemoteProductById(id: Int): ProductEntity?

    suspend fun createProductToServer(productEntity: ProductEntity)
}
package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    fun getAllProducts() :Flow<List<ProductEntity>>
    fun getProductById(id:Int):Flow<ProductEntity>
    suspend fun insertProduct(productEntity: ProductEntity)
    suspend fun insertAllProducts(productEntity: List<ProductEntity>)
    suspend fun deleteProduct(productEntity: ProductEntity)
    suspend fun deleteProductById(id: Int)
}
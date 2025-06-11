package com.dev.storeapp.domain.repository

import com.dev.storeapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    //local
    suspend fun insertProduct(productEntity: ProductEntity)
    suspend fun insertAllProduct(productEntity: List<ProductEntity>)
    suspend fun deleteProduct(productEntity: ProductEntity)
    suspend fun deleteProductById(id:Int)
    suspend fun getAllProducts() :Flow<List<ProductEntity>>
    suspend fun getProductById(id: Int) : Flow<ProductEntity?>

    //for Remote
    suspend fun getRemoteProducts(): List<ProductEntity>
    suspend fun createProductToServer(productEntity: ProductEntity)

    //for sync
    suspend fun sync() : Boolean

}
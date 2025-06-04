package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.network.StoreApi
import com.dev.storeapp.data.repository.dataSource.ProductRemoteDataSource
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(
    private val storeApi: StoreApi
): ProductRemoteDataSource {
    override suspend fun getRemoteProducts(): List<ProductEntity> {
        //always try and catch due to some network issues
        return try {
            storeApi.getAllProducts().products
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getRemoteProductById(id: Int): ProductEntity? {
        return try{
            val products = storeApi.getAllProducts().products
            products.firstOrNull {it.id == id }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createProductToServer(productEntity: ProductEntity) {
        storeApi.createProductToServer(productEntity)
    }
}
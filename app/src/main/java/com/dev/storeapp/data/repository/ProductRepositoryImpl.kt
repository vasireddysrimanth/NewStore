package com.dev.storeapp.data.repository

import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.repository.dataSource.ProductLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductRemoteDataSource
import com.dev.storeapp.domain.repository.ProductRepository
import com.dev.storeapp.presentation.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val localDataSource: ProductLocalDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource,
    @AppModule.ApplicationScope private val applicationScope: CoroutineScope
): ProductRepository{

    // i created a job for syncing data from remote to local
    // manually also it will work but in future i will add more Features so for better Apply
    override suspend fun sync(): Boolean {
        val deferredJob  = applicationScope.async{
            try {
                val products = getRemoteProducts()
                insertAllProduct(products)
                return@async true
            } catch (e: Exception) {
                return@async false
            }
        }
        return deferredJob.await()
    }

    //for local data source
    override suspend fun insertProduct(productEntity: ProductEntity) {
            localDataSource.insertProduct(productEntity)
    }

    override suspend fun insertAllProduct(productEntity: List<ProductEntity>) {
        localDataSource.insertAllProducts(productEntity)
    }

    override suspend fun deleteProduct(productEntity: ProductEntity) {
        localDataSource.deleteProduct(productEntity)
    }

    override suspend fun deleteProductById(id: Int) {
        localDataSource.deleteProductById(id)
    }

    override suspend fun getAllProducts(): Flow<List<ProductEntity>> {
        return  localDataSource.getAllProducts()
    }

    override suspend fun getProductById(id:Int): Flow<ProductEntity?> {
        return localDataSource.getProductById(id)
    }

    //for Remote
    override suspend fun getRemoteProducts(): List<ProductEntity> {
        return productRemoteDataSource.getRemoteProducts()
    }

    override suspend fun createProductToServer(productEntity: ProductEntity) {
         productRemoteDataSource.createProductToServer(productEntity)
    }
}

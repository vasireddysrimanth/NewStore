package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.dao.ProductDao
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.repository.dataSource.ProductLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val productsDao: ProductDao
):ProductLocalDataSource {
    override fun getAllProducts(): Flow<List<ProductEntity>> {
        return  productsDao.getAllProducts()
    }

    override fun getProductById(id: Int): Flow<ProductEntity?> {
        return productsDao.getProductByID(id)
    }

    override suspend fun insertProduct(productEntity: ProductEntity) {
        return productsDao.insertProduct(productEntity)
    }

    override suspend fun insertAllProducts(productEntity: List<ProductEntity>) {
        return productsDao.insertAllProducts(productEntity)
    }

    override suspend fun deleteProduct(productEntity: ProductEntity) {
        productsDao.deleteProduct(productEntity)
    }

    override suspend fun deleteProductById(id: Int) {
       productsDao.deleteProductByID(id)
    }
}
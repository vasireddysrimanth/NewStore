package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.dao.AddToCartDao
import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.data.repository.dataSource.AddToCartLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartLocalDataSourceImpl @Inject constructor(
    private val addToCartDao: AddToCartDao
) :AddToCartLocalDataSource{

    override fun getAllCarts(): Flow<List<AddToCartEntity>> {
        return addToCartDao.getAllCarts()
    }

    override suspend fun insertToCart(addToCartEntity: AddToCartEntity) {
        addToCartDao.insertAddToCart(addToCartEntity)
    }

    override suspend fun deleteAllCarts() {
        addToCartDao.deleteAllCarts()
    }

    override suspend fun deleteCartById(id: Int) {
        addToCartDao.deleteCartById(id)
    }

    override suspend fun isProductInCart(productId: Int) = addToCartDao.isProductInCart(productId)
}
package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.dao.CartSaleDao
import com.dev.storeapp.data.local.entity.CartSaleEntity
import com.dev.storeapp.data.repository.dataSource.CartSaleLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartSaleLocalDataSourceImpl @Inject constructor(
    private val dao: CartSaleDao
) :CartSaleLocalDataSource{

    override suspend fun insertCartSale(cartSaleEntity: CartSaleEntity)  = dao.insertOrUpdateCartSale(cartSaleEntity)

    override suspend fun getAllCartSales(): Flow<List<CartSaleEntity>> {
        return dao.getAllCartSales()
    }

    override suspend fun deleteCartSaleById(id: Int) = dao.deleteCartSaleById(id)

    override suspend fun deleteAllCartSales() = dao.deleteAllCartSales()

}
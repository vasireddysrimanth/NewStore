package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.CartSaleEntity
import kotlinx.coroutines.flow.Flow

interface CartSaleLocalDataSource {


    suspend fun insertCartSale(cartSaleEntity: CartSaleEntity)

    suspend fun getAllCartSales(): Flow<List<CartSaleEntity>>

    suspend fun deleteCartSaleById(id: Int)

    suspend fun deleteAllCartSales()
}
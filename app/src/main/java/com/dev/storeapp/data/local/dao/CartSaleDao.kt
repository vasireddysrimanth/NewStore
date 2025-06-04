package com.dev.storeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.local.entity.CartSaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartSaleDao {

    @Upsert
    suspend fun insertOrUpdateCartSale(cartSale: CartSaleEntity)

    @Query("SELECT * FROM ${DBConstants.CART_SALE_TABLE_NAME}")
    fun getAllCartSales(): Flow<List<CartSaleEntity>>

    @Query("DELETE FROM ${DBConstants.CART_SALE_TABLE_NAME} WHERE id = :id")
    suspend fun deleteCartSaleById(id: Int)

    @Query("DELETE FROM ${DBConstants.CART_SALE_TABLE_NAME}")
    suspend fun deleteAllCartSales()
}
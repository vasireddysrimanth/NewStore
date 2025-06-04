package com.dev.storeapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.local.entity.AddToCartEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AddToCartDao {

    @Query("SELECT * FROM ${DBConstants.TABLE_ADD_TO_CART}")
    fun getAllCarts(): Flow<List<AddToCartEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAddToCart(addToCartEntity: AddToCartEntity)

    @Query("DELETE FROM ${DBConstants.TABLE_ADD_TO_CART}")
    suspend fun deleteAllCarts()

    @Query("DELETE FROM ${DBConstants.TABLE_ADD_TO_CART} WHERE id = :id")
    suspend fun deleteCartById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM ${DBConstants.TABLE_ADD_TO_CART} WHERE id = :productId)")
    suspend fun isProductInCart(productId: Int): Boolean
}
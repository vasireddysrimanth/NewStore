package com.dev.storeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM ${DBConstants.PRODUCTS_TABLE_NAME}")
    fun getAllProducts():Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductByID(id:Int):Flow<ProductEntity>

    @Upsert
    suspend fun insertProduct(productEntity: ProductEntity)

    @Upsert
    suspend fun insertAllProducts(productEntity: List<ProductEntity>)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query("DELETE FROM ${DBConstants.PRODUCTS_TABLE_NAME} where id = :id")
    suspend fun deleteProductByID(id:Int)




}
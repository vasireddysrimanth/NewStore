package com.dev.storeapp.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.storeapp.data.local.dao.AddToCartDao
import com.dev.storeapp.data.local.dao.CartSaleDao
import com.dev.storeapp.data.local.dao.FireBaseUserDao
import com.dev.storeapp.data.local.dao.OrderDao
import com.dev.storeapp.data.local.dao.ProductDao
import com.dev.storeapp.data.local.dao.UserDao
import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.data.local.entity.CartSaleEntity
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.local.entity.UserEntity

@Database(
    entities = [
        ProductEntity::class,
        UserEntity::class,
        AddToCartEntity::class,
        CartSaleEntity::class,
        OrderEntity::class,
        FireBaseUserEntity::class
                ],
    version = 1
    , exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StoreDataBase(): RoomDatabase()  {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun addToCartDao(): AddToCartDao
    abstract fun cartSaleDao(): CartSaleDao
    abstract fun orderDao(): OrderDao
    abstract fun fireBaseUserDao(): FireBaseUserDao
}
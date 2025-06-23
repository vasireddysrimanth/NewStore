package com.dev.storeapp.presentation.di

import android.content.Context
import androidx.room.Room
import com.dev.storeapp.app.constants.Constants
import com.dev.storeapp.data.local.StoreDataBase
import com.dev.storeapp.data.local.entity.ProductEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        StoreDataBase::class.java,
        Constants.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideProductDao(db: StoreDataBase) = db.productDao()


    @Provides
    @Singleton
    fun provideUserDao(db: StoreDataBase) = db.userDao()


    @Provides
    @Singleton
    fun provideAddToCartDao(db: StoreDataBase) = db.addToCartDao()

    @Singleton
    @Provides
    fun provideOrderDao(db: StoreDataBase) = db.orderDao()

    @Singleton
    @Provides
    fun provideFireBaseUserDao(db: StoreDataBase) = db.fireBaseUserDao()

    @Provides
    fun provideProductEntity() =ProductEntity()




}
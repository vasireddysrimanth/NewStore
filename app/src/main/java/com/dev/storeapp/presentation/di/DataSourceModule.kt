package com.dev.storeapp.presentation.di

import com.dev.storeapp.data.local.dao.AddToCartDao
import com.dev.storeapp.data.local.dao.OrderDao
import com.dev.storeapp.data.local.dao.ProductDao
import com.dev.storeapp.data.local.dao.UserDao
import com.dev.storeapp.data.network.StoreApi
import com.dev.storeapp.data.network.UserApi
import com.dev.storeapp.data.repository.dataSource.AddToCartLocalDataSource
import com.dev.storeapp.data.repository.dataSource.OrderLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductRemoteDataSource
import com.dev.storeapp.data.repository.dataSource.UserLocalDataSource
import com.dev.storeapp.data.repository.dataSource.UserRemoteDataSource
import com.dev.storeapp.data.repository.dataSourceImpl.AddToCartLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.OrderLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.ProductLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.ProductRemoteDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.UserLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.UserRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource =
        UserLocalDataSourceImpl(userDao)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userApi: UserApi): UserRemoteDataSource =
        UserRemoteDataSourceImpl(userApi)

    @Singleton
    @Provides
    fun provideProductRemoteDataSource(storeApi: StoreApi): ProductRemoteDataSource =
        ProductRemoteDataSourceImpl(storeApi)

    @Singleton
    @Provides
    fun provideProductLocalDataSource(productDao: ProductDao): ProductLocalDataSource =
        ProductLocalDataSourceImpl(productDao)


    @Singleton
    @Provides
    fun provideAddToCartLocalDataSource(addToCartDao: AddToCartDao): AddToCartLocalDataSource =
        AddToCartLocalDataSourceImpl(addToCartDao)

    @Singleton
    @Provides
    fun provideOrderLocalDataSource(orderDao: OrderDao):OrderLocalDataSource =
        OrderLocalDataSourceImpl(orderDao)


}
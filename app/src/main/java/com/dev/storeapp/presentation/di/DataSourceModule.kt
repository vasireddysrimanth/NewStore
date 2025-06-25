package com.dev.storeapp.presentation.di

import com.dev.storeapp.data.local.dao.AddToCartDao
import com.dev.storeapp.data.local.dao.FireBaseUserDao
import com.dev.storeapp.data.local.dao.OrderDao
import com.dev.storeapp.data.local.dao.ProductDao
import com.dev.storeapp.data.local.dao.UserDao
import com.dev.storeapp.data.network.StoreApi
import com.dev.storeapp.data.network.UserApi
import com.dev.storeapp.data.repository.dataSource.AddToCartLocalDataSource
import com.dev.storeapp.data.repository.dataSource.FireBaseUserLocalDataSource
import com.dev.storeapp.data.repository.dataSource.FireBaseUserRemoteDataSource
import com.dev.storeapp.data.repository.dataSource.OrderLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductRemoteDataSource
import com.dev.storeapp.data.repository.dataSource.UserLocalDataSource
import com.dev.storeapp.data.repository.dataSource.UserRemoteDataSource
import com.dev.storeapp.data.repository.dataSourceImpl.AddToCartLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.FireBaseUserLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.FireBaseUsersRemoteDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.OrderLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.ProductLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.ProductRemoteDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.UserLocalDataSourceImpl
import com.dev.storeapp.data.repository.dataSourceImpl.UserRemoteDataSourceImpl
import com.dev.storeapp.presentation.utils.SharedPreferenceHelper
import com.google.firebase.firestore.FirebaseFirestore
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


    @Singleton
    @Provides
    fun provideFireBaseUserLocalDataSource(fireBaseDao: FireBaseUserDao,sharedPreferenceHelper: SharedPreferenceHelper):FireBaseUserLocalDataSource=
        FireBaseUserLocalDataSourceImpl(fireBaseDao,sharedPreferenceHelper)


    @Singleton
    @Provides
    fun provideFireBaseUserRemoteDataSource(fireBaseUserApi: FirebaseFirestore): FireBaseUserRemoteDataSource =
        FireBaseUsersRemoteDataSourceImpl(fireBaseUserApi)


}
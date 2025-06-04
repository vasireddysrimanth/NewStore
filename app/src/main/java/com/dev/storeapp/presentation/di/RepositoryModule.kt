package com.dev.storeapp.presentation.di

import com.dev.storeapp.data.repository.AddToCartRepositoryImpl
import com.dev.storeapp.data.repository.OrderRepositoryImpl
import com.dev.storeapp.data.repository.ProductRepositoryImpl
import com.dev.storeapp.data.repository.UserRepositoryImpl
import com.dev.storeapp.data.repository.dataSource.AddToCartLocalDataSource
import com.dev.storeapp.data.repository.dataSource.OrderLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductLocalDataSource
import com.dev.storeapp.data.repository.dataSource.ProductRemoteDataSource
import com.dev.storeapp.data.repository.dataSource.UserLocalDataSource
import com.dev.storeapp.data.repository.dataSource.UserRemoteDataSource
import com.dev.storeapp.domain.repository.AddToCartRepository
import com.dev.storeapp.domain.repository.OrderRepository
import com.dev.storeapp.domain.repository.ProductRepository
import com.dev.storeapp.domain.repository.UserRepository
import com.dev.storeapp.domain.useCase.AddToCartUseCase
import com.dev.storeapp.domain.useCase.ProductUseCase
import com.dev.storeapp.domain.useCase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    // Providing ProductRepository
    @Singleton
    @Provides
    fun provideProductRepository(
        productLocalDataSource: ProductLocalDataSource,
        productRemoteDataSource: ProductRemoteDataSource,
        @AppModule.ApplicationScope applicationScope: CoroutineScope

    ): ProductRepository {
        return ProductRepositoryImpl(
            productLocalDataSource, productRemoteDataSource, applicationScope
        )
    }

    // Providing UserRepository
    @Singleton
    @Provides
    fun provideUserRepository(
        userLocalDataSource: UserLocalDataSource,
        userRemoteDataSource: UserRemoteDataSource,
        @AppModule.ApplicationScope applicationScope: CoroutineScope
    ): UserRepository {
        return UserRepositoryImpl(
            userLocalDataSource, userRemoteDataSource, applicationScope
        )
    }

    @Singleton
    @Provides
    fun provideAddToCartRepository(
        addToCartLocalDataSource: AddToCartLocalDataSource
    ): AddToCartRepository {
        return AddToCartRepositoryImpl(
            addToCartLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun provideOrderRepository(
        addToCartLocalDataSource: OrderLocalDataSource
    ): OrderRepository {
        return OrderRepositoryImpl(addToCartLocalDataSource)
    }

    // Providing ProductUseCase
    @Singleton
    @Provides
    fun provideProductUseCase(repository: ProductRepository): ProductUseCase {
        return ProductUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUserUseCase(repository: UserRepository): UserUseCase {
        return UserUseCase(repository)
    }

    // Providing AddToCartUseCase
    @Singleton
    @Provides
    fun provideAddToCartUseCase(repository: AddToCartRepository): AddToCartUseCase {
        return AddToCartUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideOrderUseCase(repository: OrderRepository): com.dev.storeapp.domain.useCase.OrderUseCase {
        return com.dev.storeapp.domain.useCase.OrderUseCase(repository)
    }

}

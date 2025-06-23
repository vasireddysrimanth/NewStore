package com.dev.storeapp.data.repository

import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.repository.dataSource.FireBaseUserLocalDataSource
import com.dev.storeapp.data.repository.dataSource.FireBaseUserRemoteDataSource
import com.dev.storeapp.domain.repository.FireBaseUserRepository
import com.dev.storeapp.presentation.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FireBaseRepositoryImpl @Inject constructor(
    private val firestoreSource: FireBaseUserRemoteDataSource,
    private val fireBaseUserLocalDataSource: FireBaseUserLocalDataSource,
    @AppModule.ApplicationScope private val applicationScope: CoroutineScope
):FireBaseUserRepository {
    override suspend fun sync(): Boolean {
        val deferredJob  = applicationScope.async{
            try {
                val fireBaseUsers = getRemoteUsers()
                insertUser(fireBaseUsers)
                AppLogger.d("ProductRepository", "Products  not synced: ${fireBaseUsers.size}")
                return@async true
            } catch (e: Exception) {
                AppLogger.d("ProductRepository", "Products  not synced: ${e.localizedMessage}")
                return@async false
            }
        }
        return deferredJob.await()
    }

    override suspend fun getAllUsers(): Flow<List<FireBaseUserEntity>> {
        return  fireBaseUserLocalDataSource.getAllUsers()
    }

    override suspend fun insertUser(users: List<FireBaseUserEntity>) {
        fireBaseUserLocalDataSource.insertUser(users)
    }

    override suspend fun getRemoteUsers(): List<FireBaseUserEntity> {
        return firestoreSource.fetchAllUsers()
    }
}
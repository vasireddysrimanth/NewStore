package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import kotlinx.coroutines.flow.Flow

interface FireBaseUserLocalDataSource {
    fun getAllUsers(): Flow<List<FireBaseUserEntity>>

    suspend fun insertUser(users: List<FireBaseUserEntity>)
}
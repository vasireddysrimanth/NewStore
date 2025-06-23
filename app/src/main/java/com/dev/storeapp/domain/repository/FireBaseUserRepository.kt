package com.dev.storeapp.domain.repository

import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import kotlinx.coroutines.flow.Flow

interface FireBaseUserRepository {
    suspend fun sync(): Boolean

    suspend fun getAllUsers(): Flow<List<FireBaseUserEntity>>

    suspend fun insertUser(users: List<FireBaseUserEntity>)

    suspend fun getRemoteUsers(): List<FireBaseUserEntity>

}
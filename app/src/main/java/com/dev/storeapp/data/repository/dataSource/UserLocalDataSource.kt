package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun insertUser(user: UserEntity)

    suspend fun insertAllUsers(users: List<UserEntity>)

    fun getUserByUsername(username: String): Flow<UserEntity>

    fun getAllUsers(): Flow<List<UserEntity>>

    suspend fun deleteAllUsers()

    suspend fun deleteUserByUsername(username: String)

    fun getUserById(id: Int): Flow<UserEntity>

    suspend fun deleteUserById(id: Int)
}
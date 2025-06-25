package com.dev.storeapp.domain.repository

import com.dev.storeapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository  {

    //for local data source
    suspend fun insertUser(user: UserEntity)

    suspend fun insertAllUsers(users: List<UserEntity>)

    fun getUserByUsername(username: String): Flow<UserEntity>

    fun getAllUsers(): Flow<List<UserEntity>>

    suspend fun deleteAllUsers()

    suspend fun deleteUserByUsername(username: String)

    fun getUserById(id: Int): Flow<UserEntity>

    suspend fun deleteUserById(id: Int)

    //for remote Data Source
    suspend fun getRemoteUsers(): List<UserEntity>

    //sync
    suspend fun sync(): Boolean


}
package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.UserEntity

interface UserRemoteDataSource {
    suspend fun getUserById(id: Int): UserEntity?
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun getUserByName(name: String): UserEntity?

    suspend fun  createUserToServer(user:UserEntity)
}
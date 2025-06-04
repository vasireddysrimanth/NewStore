package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.data.network.UserApi
import com.dev.storeapp.data.repository.dataSource.UserRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl  @Inject constructor(
    private val userApi: UserApi
):UserRemoteDataSource {

    override suspend fun getUserById(id: Int): UserEntity? {
        return userApi.getAllUsers().users.firstOrNull { it.id == id }
    }

    override suspend fun getAllUsers(): List<UserEntity> {
        return userApi.getAllUsers().users
    }

    override suspend fun getUserByName(name: String): UserEntity? {
        return userApi.getAllUsers().users.firstOrNull { it.name.toString() == name }
    }

    override suspend fun createUserToServer(user: UserEntity) {
        userApi.addUserToServer(user)
    }
}

package com.dev.storeapp.data.repository.dataSource

import com.dev.storeapp.data.local.entity.FireBaseUserEntity

interface FireBaseUserRemoteDataSource {
     suspend fun fetchAllUsers(): List<FireBaseUserEntity>
     suspend fun updateUser(user: FireBaseUserEntity)

}
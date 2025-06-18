package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.dao.FireBaseUserDao
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.repository.dataSource.FireBaseUserLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FireBaseUserLocalDataSourceImpl @Inject constructor(
    private val userDao: FireBaseUserDao
):FireBaseUserLocalDataSource {
    override fun getAllUsers(): Flow<List<FireBaseUserEntity>> = userDao.getAllUsers()

    override suspend fun insertUser(users: List<FireBaseUserEntity>) = userDao.insertUsers(users)
}
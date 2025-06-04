package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.dao.UserDao
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.data.repository.dataSource.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserLocalDataSource {

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun insertAllUsers(users: List<UserEntity>) {
        userDao.insertAllUsers(users)
    }

    override fun getUserByUsername(username: String): Flow<UserEntity> {
        return userDao.getUserByUsername(username)
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

    override suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    override suspend fun deleteUserByUsername(username: String) {
        userDao.deleteUserByUsername(username)
    }

    override fun getUserById(id: Int): Flow<UserEntity> {
        return userDao.getUserById(id)
    }

    override suspend fun deleteUserById(id: Int) {
        userDao.deleteUserById(id)
    }

}
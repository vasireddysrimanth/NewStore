package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.app.constants.Constants
import com.dev.storeapp.data.local.dao.FireBaseUserDao
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.repository.dataSource.FireBaseUserLocalDataSource
import com.dev.storeapp.presentation.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FireBaseUserLocalDataSourceImpl @Inject constructor(
    private val userDao: FireBaseUserDao,
    private val sharedPreferenceHelper: SharedPreferenceHelper
):FireBaseUserLocalDataSource {
    override fun getAllUsers(): Flow<List<FireBaseUserEntity>> = userDao.getAllUsers()

    override suspend fun insertUser(users: List<FireBaseUserEntity>) = userDao.insertUsers(users)

    override suspend fun getUserNameByEmail(): String {
        val email = sharedPreferenceHelper.get(Constants.LOGGED_IN_USER_EMAIL)
        return userDao.getUserNameByEmail(email) ?: email
    }

    override  fun getUserByEmail() = userDao.getUserByEmail(sharedPreferenceHelper.get(Constants.LOGGED_IN_USER_EMAIL))
    override suspend fun upsertUser(userEntity: FireBaseUserEntity) =userDao.upsertUser(userEntity)
}
package com.dev.storeapp.data.repository

import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.data.repository.dataSource.UserLocalDataSource
import com.dev.storeapp.data.repository.dataSource.UserRemoteDataSource
import com.dev.storeapp.domain.repository.UserRepository
import com.dev.storeapp.presentation.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
    @AppModule.ApplicationScope private val applicationScope: CoroutineScope
):UserRepository{


    override suspend fun sync(): Boolean {
        val deferredJob = applicationScope.async {
            try{
                val remoteUsers =getRemoteUsers()
                insertAllUsers(remoteUsers)
                return@async true
            }catch (e :Exception){
                e.printStackTrace()
                return@async false
            }
        }
        return deferredJob.await()
    }

    //for local data source
    override suspend fun insertUser(user: UserEntity) {
        userLocalDataSource.insertUser(user)
    }

    override suspend fun insertAllUsers(users: List<UserEntity>) {
        userLocalDataSource.insertAllUsers(users)
    }

    override fun getUserByUsername(username: String): Flow<UserEntity> {
       return userLocalDataSource.getUserByUsername(username)
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
       return userLocalDataSource.getAllUsers()
    }

    override suspend fun deleteAllUsers() {
        return userLocalDataSource.deleteAllUsers()
    }

    override suspend fun deleteUserByUsername(username: String) {
        return userLocalDataSource.deleteUserByUsername(username)
    }

    override fun getUserById(id: Int): Flow<UserEntity> {
        return userLocalDataSource.getUserById(id)
    }

    override suspend fun deleteUserById(id: Int) {
        return userLocalDataSource.deleteUserById(id)
    }

    //for remote
    override suspend fun getRemoteUsers(): List<UserEntity> {
        val remoteUsers = remoteDataSource.getAllUsers()
        if (remoteUsers.isNotEmpty()) {
            userLocalDataSource.insertAllUsers(remoteUsers)
        }
        return remoteUsers
    }

    override suspend fun createUserToServer(user: UserEntity) {
            remoteDataSource.createUserToServer(user)
    }

}
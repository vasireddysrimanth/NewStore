package com.dev.storeapp.domain.useCase

import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.domain.repository.FireBaseUserRepository
import com.dev.storeapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val fireBaseUserRepository: FireBaseUserRepository
) {


    suspend fun sync() :Boolean = repository.sync()

    fun getUserByUsername(username: String): Flow<UserEntity> = repository.getUserByUsername(username)

    fun getAllUsers(): Flow<List<UserEntity>> = repository.getAllUsers()

    suspend fun deleteAllUsers() = repository.deleteAllUsers()

    suspend fun deleteUserByUsername(username: String) = repository.deleteUserByUsername(username)

    fun getUserById(id: Int): Flow<UserEntity> = repository.getUserById(id)

    suspend fun deleteUserById(id: Int) = repository.deleteUserById(id)

    suspend fun insertUser(user:UserEntity) = repository.insertUser(user)

    suspend fun getUserNameByEmail(): String? {
        return fireBaseUserRepository.getUserNameByEmail()
    }

    suspend fun getFirebaseUsers() = fireBaseUserRepository.getAllUsers()
    fun getUserByEmail(): Flow<FireBaseUserEntity> = fireBaseUserRepository.getUserByEmail()
    suspend fun upsertUser(user : FireBaseUserEntity) = fireBaseUserRepository.upsertUser(user)




}
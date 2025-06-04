package com.dev.storeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dev.storeapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Upsert
    suspend fun insertAllUsers(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): Flow<UserEntity>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteUserByUsername(username: String)

    @Query("SELECT * FROM users WHERE id = :id")
     fun getUserById(id: Int): Flow<UserEntity>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)

}
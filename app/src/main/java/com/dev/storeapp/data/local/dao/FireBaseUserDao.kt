package com.dev.storeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FireBaseUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<FireBaseUserEntity>)

    @Query("SELECT * FROM ${DBConstants.FIREBASE_USER_ENTITY}")
     fun getAllUsers(): Flow<List<FireBaseUserEntity>>

     @Query("SELECT username FROM ${DBConstants.FIREBASE_USER_ENTITY} WHERE email = :email")
     fun getUserNameByEmail(email: String): String?

}
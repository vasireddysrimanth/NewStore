package com.dev.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.model.FireBaseUser

@Entity(DBConstants.FIREBASE_USER_ENTITY)
data class FireBaseUserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val gender: String = "",
    val created_at: Long = 0
)

fun FireBaseUserEntity.toFireBaseUser()=FireBaseUser(
    id = id,
    uid = uid,
    email = email,
    username = username,
    gender = gender,
    created_at = created_at
)
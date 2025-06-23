package com.dev.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.model.FireBaseUser
import com.google.firebase.Timestamp

@Entity(DBConstants.FIREBASE_USER_ENTITY)
data class FireBaseUserEntity(
    @PrimaryKey
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val gender: String = "",
    val createdAt: Timestamp? = null
)

fun FireBaseUserEntity.toFireBaseUser() = FireBaseUser(
    uid = uid,
    email = email,
    username = username,
    gender = gender,
    createdAt = createdAt
)
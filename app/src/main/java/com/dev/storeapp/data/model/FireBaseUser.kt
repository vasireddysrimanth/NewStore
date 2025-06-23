package com.dev.storeapp.data.model

import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.google.firebase.Timestamp

class FireBaseUser(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val gender: String = "",
    val createdAt: Timestamp? = null
)

fun FireBaseUser.toFireBaseUserEntity()= FireBaseUserEntity(
    uid = uid,
    email = email,
    username = username,
    gender = gender,
    createdAt = createdAt
)
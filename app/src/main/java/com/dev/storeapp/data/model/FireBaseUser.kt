package com.dev.storeapp.data.model

import com.dev.storeapp.data.local.entity.FireBaseUserEntity

class FireBaseUser(
    val id: Int = 0,
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val gender: String = "",
    val created_at: Long = 0
)

fun FireBaseUser.toFireBaseUserEntity()= FireBaseUserEntity(
    id = id,
    uid = uid,
    email = email,
    username = username,
    gender = gender,
    created_at = created_at
)
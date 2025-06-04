package com.dev.storeapp.data.network.apiresponse

import com.dev.storeapp.data.local.entity.UserEntity
import com.google.gson.annotations.SerializedName

data  class UserResponse (
    @SerializedName("users") var users : ArrayList<UserEntity> = arrayListOf()
)

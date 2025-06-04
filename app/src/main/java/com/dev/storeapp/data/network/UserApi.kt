package com.dev.storeapp.data.network

import com.dev.storeapp.app.constants.ApiConstnats
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.data.network.apiresponse.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET(ApiConstnats.GET_ALL_USERS)
    suspend fun getAllUsers(): UserResponse

    @POST(ApiConstnats.GET_ALL_USERS)
    suspend fun addUserToServer(@Body userEntity: UserEntity):UserResponse

}
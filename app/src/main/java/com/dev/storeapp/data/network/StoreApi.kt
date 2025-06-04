package com.dev.storeapp.data.network

import com.dev.storeapp.app.constants.ApiConstnats
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.network.apiresponse.ProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoreApi {
    @GET(ApiConstnats.GET_ALL_PRODUCTS)
    suspend fun getAllProducts(): ProductResponse

    @POST(ApiConstnats.GET_ALL_PRODUCTS)
    suspend fun createProductToServer(@Body productEntity: ProductEntity) :ProductResponse
}
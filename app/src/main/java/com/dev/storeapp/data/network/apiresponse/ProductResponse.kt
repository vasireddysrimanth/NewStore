package com.dev.storeapp.data.network.apiresponse

import com.dev.storeapp.data.local.entity.ProductEntity
import com.google.gson.annotations.SerializedName

data  class ProductResponse (
    @SerializedName("products" ) var products : ArrayList<ProductEntity> = arrayListOf(),
):BaseResponse()

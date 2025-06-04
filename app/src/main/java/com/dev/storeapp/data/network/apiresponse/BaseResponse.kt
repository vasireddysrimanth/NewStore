package com.dev.storeapp.data.network.apiresponse

import android.util.Log
import com.dev.storeapp.app.constants.Constants
import com.google.gson.annotations.SerializedName

open class BaseResponse (
    @SerializedName("message" ) var message : String          = "",
    @SerializedName("status"    ) var statusCode    : String             = ""
){
    fun isSuccess() : Boolean {

        if(statusCode!=Constants.SUCCESS_CODE)
        {
            Log.e("Api Response","API Error Response : $message")
        }
        return this.statusCode == Constants.SUCCESS_CODE
    }
}

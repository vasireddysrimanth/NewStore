package com.dev.storeapp.app.common


data class DataStatus<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val isEmpty: Boolean? = false
) {

    enum class Status {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    companion object{
        fun <T> loading():DataStatus<T>{
            return DataStatus(Status.LOADING)
        }
        fun <T> success(data:T?):DataStatus<T>{
            return DataStatus(Status.SUCCESS,data)
        }
        fun <T> error(message: String?):DataStatus<T>{
            return DataStatus(Status.ERROR,message = message)
        }
        fun <T> empty(): DataStatus<T> {
            return DataStatus(Status.EMPTY)
        }
    }
}

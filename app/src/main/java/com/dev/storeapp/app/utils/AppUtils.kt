package com.dev.storeapp.app.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

object AppUtils {
    fun getGuid() = UUID.randomUUID().toString()



    private const val DEFAULT_DATE_FORMAT                               = "yyyy-MM-dd'T'HH:mm:ss.SS"

    private val inputDateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US)

    fun now(): Timestamp {
        return Timestamp(System.currentTimeMillis())
    }

    fun currentDate() : String{
        return inputDateFormat.format(now())
    }
}
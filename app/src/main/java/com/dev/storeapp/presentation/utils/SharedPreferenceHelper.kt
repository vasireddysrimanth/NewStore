package com.dev.storeapp.presentation.utils

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SharedPreferenceHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun put(key:String,value: String?){
        sharedPreferences.edit { putString(key, value) }
    }

    fun get(
        key: String,
        defaultValue: String? = null
    ): String {
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }


}
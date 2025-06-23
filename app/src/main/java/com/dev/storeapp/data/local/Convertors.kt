package com.dev.storeapp.data.local

import androidx.room.TypeConverter
import com.dev.storeapp.data.local.entity.Address
import com.dev.storeapp.data.local.entity.Name
import com.dev.storeapp.data.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.Timestamp

class Converters {

    @TypeConverter
    fun fromAddress(address: Address): String {
        return Gson().toJson(address)
    }

    @TypeConverter
    fun toAddress(addressString: String): Address {
        return Gson().fromJson(addressString, Address::class.java)
    }

    @TypeConverter
    fun fromName(name: Name): String {
        return Gson().toJson(name)
    }

    @TypeConverter
    fun toName(nameString: String): Name {
        return Gson().fromJson(nameString, Name::class.java)
    }

    @TypeConverter
    fun fromProductList(products: List<Product>): String {
        return Gson().toJson(products)
    }

    @TypeConverter
    fun toProductList(productsJson: String): List<Product> {
        val listType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(productsJson, listType)
    }


    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long? {
        return value?.seconds?.times(1000)
    }

    @TypeConverter
    fun toTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it / 1000, 0) }
    }
}
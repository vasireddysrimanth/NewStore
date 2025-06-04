package com.dev.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.model.Address as AddressModel
import com.dev.storeapp.data.model.Name as NameModel
import com.dev.storeapp.data.model.User
import com.google.gson.annotations.SerializedName


@Entity(tableName = DBConstants.USERS_TABLE_NAME)
data class UserEntity(
    @SerializedName("id") @PrimaryKey val id: Int = 0,
    @SerializedName("address") var address: Address = Address(),
    @SerializedName("email") var email: String = "",
    @SerializedName("name") var name: Name = Name(),
    @SerializedName("password") val password: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("username") var username: String = ""
)

data class Name(
    @SerializedName("firstname") val firstname: String = "",
    @SerializedName("lastname") val lastname: String = ""
)

data class Address(
    @SerializedName("city") val city: String = "",
    @SerializedName("number") val number: String = "",
    @SerializedName("street") val street: String = "",
    @SerializedName("zipcode") val zipcode: String = ""
)

fun UserEntity.toUser() = User(
    id = id,
    address = AddressModel(
        city = address.city,
        number = address.number,
        street = address.street,
        zipcode = address.zipcode
    ),
    email = email,
    name = NameModel(
        firstname = name.firstname,
        lastname = name.lastname
    ),
    password = password,
    phone = phone,
    username = username
)
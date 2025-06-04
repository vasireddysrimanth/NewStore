package com.dev.storeapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val address: Address,
    val email: String,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
) :Parcelable


@Parcelize
data class Name(
    val firstname: String,
    val lastname: String
):Parcelable


@Parcelize
data class Address(
    val city: String,
    val number: String,
    val street: String,
    val zipcode: String
):Parcelable

package com.dev.storeapp.data.model

import android.os.Parcelable
import com.dev.storeapp.data.local.entity.AddToCartEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val brand: String,
    val category: String,
    val color: String,
    val description: String,
    val discount: Int,
    val image: String,
    val model: String,
    val price: Double,
    var quantity: Int = 1
): Parcelable

fun Product.toAddToCartEntity () :AddToCartEntity{
    return AddToCartEntity(
        id = id,
        title = title,
        brand = brand,
        category = category,
        color = color,
        description = description,
        discount = discount,
        image = image,
        model = model,
        price = price,
        quantity = 1
    )
}
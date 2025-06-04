package com.dev.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.storeapp.app.constants.DBConstants
import com.dev.storeapp.data.model.Product
import com.google.gson.annotations.SerializedName

@Entity(tableName = DBConstants.TABLE_ADD_TO_CART)
data class AddToCartEntity(
    @SerializedName("id") @PrimaryKey var id: Int = 0,
    @SerializedName("title") var title: String? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("color") var color: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("discount") var discount: Int? = 0,
    @SerializedName("image") var image: String? = null,
    @SerializedName("model") var model: String? = null,
    @SerializedName("price") var price: Double? = 0.0,
    @SerializedName("quantity") var quantity: Int? = 0
)

fun AddToCartEntity.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        brand = brand,
        category = category,
        color = color,
        description = description,
        discount = discount,
        image = image,
        model = model,
        price = price
    )
}

fun AddToCartEntity.asProductModel() = Product(
    id = id,
    title = title ?: "",
    brand = brand ?: "",
    category = category ?: "",
    color = color ?: "",
    description = description ?: "",
    discount = discount ?: 0,
    image = image ?: "",
    model = model ?: "",
    price = price ?: 0.0,
)


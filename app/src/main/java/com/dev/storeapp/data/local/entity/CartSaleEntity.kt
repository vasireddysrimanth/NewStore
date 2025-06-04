package com.dev.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.storeapp.app.constants.DBConstants
import com.google.gson.annotations.SerializedName

@Entity(DBConstants.CART_SALE_TABLE_NAME)
data class CartSaleEntity(
    @SerializedName("id"            ) @PrimaryKey var id: Int = 0,
    @SerializedName("title"         )    var title:          String? = null,
    @SerializedName("brand"         )    var brand:          String? = null,
    @SerializedName("category"      )    var category:       String? = null,
    @SerializedName("color"         )    var color:          String? = null,
    @SerializedName("description"   )    var description:    String? = null,
    @SerializedName("discount"      )    var discount:       Int? = 0,
    @SerializedName("image"         )    var image:          String? = null,
    @SerializedName("model"         )    var model:          String? = null,
    @SerializedName("quantity"      )    var quantity:       Int = 1,
    @SerializedName("price"         )    var price:          Int?= 0,
)



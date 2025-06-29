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

/**
 * Convert Store Product to Payment Module Product
 */
fun mapToPaymentProduct(storeProduct: Product): com.dev.payment.payment.Product {
    return com.dev.payment.payment.Product(
        id = storeProduct.id,
        title = storeProduct.title,
        price = storeProduct.price,
        description = storeProduct.description,
        category = storeProduct.category,
        image = storeProduct.image,
        quantity = storeProduct.quantity,
        brand = storeProduct.brand,
        color = storeProduct.color,
        discount = storeProduct.discount,
        model = storeProduct.model
    )
}

/**
 * Convert Payment Module Product back to Store Product
 */
fun mapToStoreProduct(paymentProduct: com.dev.payment.payment.Product): Product {
    return Product(
        id = paymentProduct.id,
        title = paymentProduct.title,
        price = paymentProduct.price,
        description = paymentProduct.description,
        category = paymentProduct.category,
        image = paymentProduct.image,
        quantity = paymentProduct.quantity,
        brand = paymentProduct.brand,
        color = paymentProduct.color,
        discount = paymentProduct.discount,
        model = paymentProduct.model
    )
}

/**
 * Convert list of Store Products to Payment Module Products
 */
fun mapToPaymentProducts(storeProducts: List<Product>): List<com.dev.payment.payment.Product> {
    return storeProducts.map { mapToPaymentProduct(it) }
}

/**
 * Convert list of Payment Module Products back to Store Products
 */
fun mapToStoreProducts(paymentProducts: List<com.dev.payment.payment.Product>): List<Product> {
    return paymentProducts.map { mapToStoreProduct(it) }
}
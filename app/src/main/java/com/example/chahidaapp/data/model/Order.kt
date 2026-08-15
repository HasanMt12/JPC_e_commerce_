package com.example.chahidaapp.data.model

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("customer_name") val customerName: String,
    @SerializedName("customer_phone") val customerPhone: String,
    @SerializedName("customer_address") val customerAddress: String,
    @SerializedName("delivery_type") val deliveryType: String,
    @SerializedName("delivery_charge") val deliveryCharge: Double,
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("total") val total: Double,
    @SerializedName("items") val items: List<OrderItem>
)

data class OrderItem(
    @SerializedName("product_id") val productId: String? = null,
    @SerializedName("variant_id") val variantId: String? = null,
    @SerializedName("quantity") val quantity: Int? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("product_title") val productTitle: String? = null,
    @SerializedName("product_image") val productImage: String? = null
)

data class OrderResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: OrderData? = null
)

data class OrderData(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("customer_name") val customerName: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("total") val total: Double? = null,
    @SerializedName("items") val items: List<OrderItem>? = emptyList()
)

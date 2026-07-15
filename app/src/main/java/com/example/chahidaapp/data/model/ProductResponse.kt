package com.example.yourappname.data.model

import com.google.gson.annotations.SerializedName

// Top-level response wrapper class
data class ProductBaseResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<ProductItem>
)

// Individual Product Data Class
data class ProductItem(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String, // HTML Content embedded string
    @SerializedName("status") val status: String,
    @SerializedName("is_featured") val isFeatured: Boolean,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("views") val views: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("extra_images") val extraImages: List<String>,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("variants") val variants: List<ProductVariant>,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updated_at: String
)

// Product Variants (e.g., 250gm, 500gm, 2kg with different prices)
data class ProductVariant(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String, // "2kg", "250gm" etc
    @SerializedName("specification") val specification: String,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("photo") val photo: String,
    @SerializedName("deliveryCharge") val deliveryCharge: Double,
    @SerializedName("isDeliveryFree") val isDeliveryFree: Boolean
)
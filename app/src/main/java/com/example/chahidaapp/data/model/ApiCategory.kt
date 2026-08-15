package com.example.chahidaapp.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<ApiCategory>
)

data class ApiCategory(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("hidden") val hidden: Boolean = false
)

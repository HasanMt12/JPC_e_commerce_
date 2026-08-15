package com.example.chahidaapp.data.model

import com.google.gson.annotations.SerializedName

data class FaqResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Faq>
)

data class Faq(
    @SerializedName("_id") val id: String,
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("order") val order: Int = 0,
    @SerializedName("created_at") val createdAt: String? = null
)

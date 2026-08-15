package com.example.chahidaapp.data.model

import com.google.gson.annotations.SerializedName

data class WebsiteInfoResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: WebsiteInfo
)

data class WebsiteInfo(
    @SerializedName("_id") val id: String,
    @SerializedName("logo_url") val logoUrl: String,
    @SerializedName("about_short") val aboutShort: String,
    @SerializedName("facebook_url") val facebookUrl: String,
    @SerializedName("whatsapp_number") val whatsappNumber: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("messenger_url") val messengerUrl: String? = null
)

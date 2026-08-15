package com.example.chahidaapp.data.api


import com.example.chahidaapp.data.model.CategoryResponse
import com.example.chahidaapp.data.model.FaqResponse
import com.example.chahidaapp.data.model.OrderRequest
import com.example.chahidaapp.data.model.OrderResponse
import com.example.chahidaapp.data.model.ProductBaseResponse
import com.example.chahidaapp.data.model.WebsiteInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrganicApiService {

    @GET("categories")
    suspend fun getCategories(): CategoryResponse

    @GET("products")
    suspend fun getProducts(): ProductBaseResponse

    @GET("faq")
    suspend fun getFaqs(): FaqResponse

    @POST("orders")
    suspend fun placeOrder(@Body order: OrderRequest): OrderResponse

    @GET("website-info")
    suspend fun getWebsiteInfo(): WebsiteInfoResponse
}
package com.example.chahidaapp.data.api


import com.example.chahidaapp.data.model.CategoryResponse
import com.example.chahidaapp.data.model.ProductBaseResponse
import retrofit2.http.GET

interface OrganicApiService {

    @GET("categories")
    suspend fun getCategories(): CategoryResponse
    @GET("products") //  endpoint (e.g. "api/v1/products")
    suspend fun getProducts(): ProductBaseResponse
}
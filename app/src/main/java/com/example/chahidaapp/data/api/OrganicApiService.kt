package com.example.chahidaapp.data.api


import com.example.yourappname.data.model.ProductBaseResponse
import retrofit2.http.GET

interface OrganicApiService {

    // Apnar dynamic response processing check endpoint logic line
    @GET("products") // Apnar base URL er porer real endpoint-ta ekhane boshbe (e.g. "api/v1/products")
    suspend fun getProducts(): ProductBaseResponse
}
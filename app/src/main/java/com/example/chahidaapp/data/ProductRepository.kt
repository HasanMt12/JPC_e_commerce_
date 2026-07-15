package com.example.chahidaapp.data

import com.example.chahidaapp.data.api.RetrofitClient
import com.example.yourappname.data.model.ProductBaseResponse


class ProductRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun fetchProducts(): ProductBaseResponse {
        return apiService.getProducts()
    }
}
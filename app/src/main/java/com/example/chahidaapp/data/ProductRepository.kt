package com.example.chahidaapp.data

import com.example.chahidaapp.data.api.RetrofitClient
import com.example.chahidaapp.data.model.CategoryResponse
import com.example.chahidaapp.data.model.ProductBaseResponse


class ProductRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun fetchProducts(): ProductBaseResponse {
        return apiService.getProducts()
    }

    suspend fun fetchCategories(): CategoryResponse {
        return apiService.getCategories()
    }

    suspend fun fetchFaqs(): com.example.chahidaapp.data.model.FaqResponse {
        return apiService.getFaqs()
    }

    suspend fun fetchWebsiteInfo(): com.example.chahidaapp.data.model.WebsiteInfoResponse {
        return apiService.getWebsiteInfo()
    }
}
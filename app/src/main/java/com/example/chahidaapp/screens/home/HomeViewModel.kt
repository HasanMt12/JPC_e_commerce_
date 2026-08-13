package com.example.chahidaapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chahidaapp.data.ProductRepository
import com.example.chahidaapp.data.model.ApiCategory
import com.example.chahidaapp.data.model.ProductItem
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Screen resource UI loading states wrapper
sealed class HomeUiState {
    object Loading : HomeUiState()
    // 🌟 Success স্টেটে এখন products এবং categories দুটোই থাকবে
    data class Success(
        val products: List<ProductItem>,
        val categories: List<ApiCategory>
    ) : HomeUiState()
    data class Error(val errorMessage: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                // ⚡ async ব্যবহার করে প্রোডাক্ট এবং ক্যাটাগরি দুইটা কলই একসাথে স্টার্ট হবে (প্যারালাল কল)
                val productsDeferred = async { repository.fetchProducts() }
                val categoriesDeferred = async { repository.fetchCategories() }

                // দুটির রেজাল্ট একসাথে রিসিভ করা হচ্ছে
                val productResponse = productsDeferred.await()
                val categoryResponse = categoriesDeferred.await()

                if (productResponse.success && categoryResponse.success) {
                    // সফল হলে দুটো ডেটাই সাকসেস স্টেটে পাঠিয়ে দেওয়া হচ্ছে
                    _uiState.value = HomeUiState.Success(
                        products = productResponse.data,
                        categories = categoryResponse.data
                    )
                } else {
                    _uiState.value = HomeUiState.Error("Data load unsuccessfully.")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown network error occurred.")
            }
        }
    }
}
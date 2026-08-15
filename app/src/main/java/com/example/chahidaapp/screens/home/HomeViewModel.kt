package com.example.chahidaapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chahidaapp.data.ProductRepository
import com.example.chahidaapp.data.model.ApiCategory
import com.example.chahidaapp.data.model.Faq
import com.example.chahidaapp.data.model.ProductItem
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val products: List<ProductItem>,
        val categories: List<ApiCategory>,
        val faqs: List<Faq> = emptyList()
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
                val productsDeferred = async { repository.fetchProducts() }
                val categoriesDeferred = async { repository.fetchCategories() }
                val faqsDeferred = async { 
                    try { repository.fetchFaqs() } catch (e: Exception) { null }
                }

                val productResponse = productsDeferred.await()
                val categoryResponse = categoriesDeferred.await()
                val faqResponse = faqsDeferred.await()

                if (productResponse.success && categoryResponse.success) {
                    _uiState.value = HomeUiState.Success(
                        products = productResponse.data,
                        categories = categoryResponse.data,
                        faqs = faqResponse?.data ?: emptyList()
                    )
                } else {
                    _uiState.value = HomeUiState.Error("Data load unsuccessful.")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown network error occurred.")
            }
        }
    }
}

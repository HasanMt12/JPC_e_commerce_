package com.example.chahidaapp.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chahidaapp.data.ProductRepository
import com.example.chahidaapp.data.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProductDetailsUiState {
    object Loading : ProductDetailsUiState()
    data class Success(val product: ProductItem) : ProductDetailsUiState()
    data class Error(val message: String) : ProductDetailsUiState()
}

class ProductDetailsViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading
            try {
                val response = repository.fetchProducts()
                if (response.success) {
                    val product = response.data.find { it.id == productId }
                    if (product != null) {
                        _uiState.value = ProductDetailsUiState.Success(product)
                    } else {
                        _uiState.value = ProductDetailsUiState.Error("Product not found")
                    }
                } else {
                    _uiState.value = ProductDetailsUiState.Error("Failed to fetch product")
                }
            } catch (e: Exception) {
                _uiState.value = ProductDetailsUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

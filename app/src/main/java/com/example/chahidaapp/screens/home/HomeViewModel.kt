package com.example.chahidaapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chahidaapp.data.ProductRepository
import com.example.yourappname.data.model.ProductItem

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Screen resource UI loading states wrapper
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<ProductItem>) : HomeUiState()
    data class Error(val errorMessage: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = repository.fetchProducts()
                if (response.success) {
                    _uiState.value = HomeUiState.Success(response.data)
                } else {
                    _uiState.value = HomeUiState.Error("Data load unsuccessfully.")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown network error occured.")
            }
        }
    }
}
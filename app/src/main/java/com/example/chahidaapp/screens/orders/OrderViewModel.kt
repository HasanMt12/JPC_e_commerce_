package com.example.chahidaapp.screens.orders

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chahidaapp.data.api.RetrofitClient
import com.example.chahidaapp.data.model.OrderData
import com.example.chahidaapp.data.model.OrderRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OrderUiState {
    object Idle : OrderUiState()
    object Loading : OrderUiState()
    data class Success(val order: OrderData) : OrderUiState()
    data class Error(val message: String) : OrderUiState()
}

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.apiService
    private val sharedPrefs = application.getSharedPreferences("chahida_orders", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _orderState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val orderState: StateFlow<OrderUiState> = _orderState.asStateFlow()

    private val _localOrders = MutableStateFlow<List<OrderData>>(emptyList())
    val localOrders: StateFlow<List<OrderData>> = _localOrders.asStateFlow()

    init {
        loadLocalOrders()
    }

    fun placeOrder(orderRequest: OrderRequest) {
        viewModelScope.launch {
            _orderState.value = OrderUiState.Loading
            try {
                val response = apiService.placeOrder(orderRequest)
                if (response.success && response.data != null) {
                    // Update the response data with item details from request before saving
                    val orderWithDetails = response.data.copy(
                        total = orderRequest.total,
                        items = orderRequest.items
                    )
                    _orderState.value = OrderUiState.Success(orderWithDetails)
                    saveOrderLocally(orderWithDetails)
                } else {
                    _orderState.value = OrderUiState.Error("অর্ডার প্লেস করতে সমস্যা হয়েছে")
                }
            } catch (e: Exception) {
                _orderState.value = OrderUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    private fun saveOrderLocally(order: OrderData) {
        val currentOrders = getLocalOrdersFromPrefs().toMutableList()
        currentOrders.add(0, order)
        val json = gson.toJson(currentOrders)
        sharedPrefs.edit().putString("orders_list", json).apply()
        _localOrders.value = currentOrders
    }

    private fun loadLocalOrders() {
        _localOrders.value = getLocalOrdersFromPrefs()
    }

    private fun getLocalOrdersFromPrefs(): List<OrderData> {
        return try {
            val json = sharedPrefs.getString("orders_list", null) ?: return emptyList()
            val type = object : TypeToken<List<OrderData>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun resetOrderState() {
        _orderState.value = OrderUiState.Idle
    }
}

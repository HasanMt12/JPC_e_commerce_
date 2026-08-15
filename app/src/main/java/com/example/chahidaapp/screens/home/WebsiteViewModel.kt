package com.example.chahidaapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chahidaapp.data.ProductRepository
import com.example.chahidaapp.data.model.WebsiteInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WebsiteViewModel : ViewModel() {
    private val repository = ProductRepository()
    private val _websiteInfo = MutableStateFlow<WebsiteInfo?>(null)
    val websiteInfo: StateFlow<WebsiteInfo?> = _websiteInfo

    init {
        fetchInfo()
    }

    private fun fetchInfo() {
        viewModelScope.launch {
            try {
                val response = repository.fetchWebsiteInfo()
                if (response.success) {
                    _websiteInfo.value = response.data
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

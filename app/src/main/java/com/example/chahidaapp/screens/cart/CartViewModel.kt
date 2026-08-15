package com.example.chahidaapp.screens.cart

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.chahidaapp.data.model.ProductItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data model for items in cart
data class CartItem(
    val product: ProductItem,
    val quantity: Int = 1
)

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("chahida_cart", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(loadCart())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun getTotalItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun addToCart(product: ProductItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }
            val newList = if (existingItem != null) {
                currentList.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = item.quantity + 1)
                    } else item
                }
            } else {
                currentList + CartItem(product = product, quantity = 1)
            }
            saveCart(newList)
            newList
        }
    }

    fun removeFromCart(product: ProductItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }
            val newList = if (existingItem != null && existingItem.quantity > 1) {
                currentList.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = item.quantity - 1)
                    } else item
                }
            } else {
                currentList.filterNot { it.product.id == product.id }
            }
            saveCart(newList)
            newList
        }
    }

    fun deleteCartItem(product: ProductItem) {
        _cartItems.update { currentList ->
            val newList = currentList.filterNot { it.product.id == product.id }
            saveCart(newList)
            newList
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        saveCart(emptyList())
    }

    private fun saveCart(items: List<CartItem>) {
        val json = gson.toJson(items)
        sharedPrefs.edit().putString("cart_items", json).apply()
    }

    private fun loadCart(): List<CartItem> {
        val json = sharedPrefs.getString("cart_items", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type)
    }
}

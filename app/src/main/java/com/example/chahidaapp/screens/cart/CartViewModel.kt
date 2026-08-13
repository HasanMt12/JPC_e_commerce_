package com.example.chahidaapp.screens.cart

import androidx.lifecycle.ViewModel
import com.example.chahidaapp.data.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data model for items in cart
data class CartItem(
    val product: ProductItem,
    val quantity: Int = 1
)

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Total badge count (e.g. 1 + 2 = 3 total items)
    fun getTotalItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    // Add product to cart (or increment quantity if already added)
    fun addToCart(product: ProductItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }
            if (existingItem != null) {
                currentList.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = item.quantity + 1)
                    } else item
                }
            } else {
                currentList + CartItem(product = product, quantity = 1)
            }
        }
    }

    // Decrease item quantity (removes item if quantity reaches 0)
    fun removeFromCart(product: ProductItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }
            if (existingItem != null && existingItem.quantity > 1) {
                currentList.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = item.quantity - 1)
                    } else item
                }
            } else {
                currentList.filterNot { it.product.id == product.id }
            }
        }
    }

    // Remove single item completely regardless of quantity
    fun deleteCartItem(product: ProductItem) {
        _cartItems.update { currentList ->
            currentList.filterNot { it.product.id == product.id }
        }
    }

    // Clear whole cart
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
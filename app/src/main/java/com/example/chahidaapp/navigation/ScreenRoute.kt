package com.example.chahidaapp.navigation

// navigation/ScreenRoute.kt
sealed class ScreenRoute(val route: String) {
    object Home : ScreenRoute("home_screen")
    object Details : ScreenRoute("details_screen/{productId}") {
        fun passProductId(id: Int) = "details_screen/$id"
    }
    object Cart : ScreenRoute("cart_screen")
}
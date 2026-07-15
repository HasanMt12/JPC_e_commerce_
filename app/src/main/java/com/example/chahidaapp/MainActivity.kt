package com.example.chahidaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chahidaapp.screens.cart.CartScreen
import com.example.chahidaapp.screens.details.ProductDetailsScreen
import com.example.chahidaapp.screens.home.HomeScreen
import com.example.chahidaapp.ui.theme.ChahidaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChahidaAppTheme {
                MainAppStructure()
            }
        }
    }
}

// বটম নেভিগেশনের আইটেমগুলোর জন্য একটি সিলড ক্লাস
sealed class BottomNavItem(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Home : BottomNavItem("home", "হোম", { Icon(Icons.Default.Home, contentDescription = "Home") })
    object Cart : BottomNavItem("cart", "কার্ট", { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") })
    object Orders : BottomNavItem("orders", "অর্ডার", { Icon(Icons.Default.List, contentDescription = "Orders") })
}

@Composable
fun MainAppStructure() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // বটম বারে যে আইটেমগুলো দেখাতে চাই
    val bottomNavigationItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Orders
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // আমরা তখনই বটম বার দেখাবো যখন ইউজার Home, Cart অথবা Orders স্ক্রিনে থাকবে।
            // Details স্ক্রিনে গেলে বটম বার লুকিয়ে যাবে।
            if (currentRoute in listOf("home", "cart", "orders")) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color(0xFF4CAF50) // Organic Green Theme
                ) {
                    bottomNavigationItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            label = { Text(item.title) },
                            icon = item.icon,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2E7D32),
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = Color(0xFF2E7D32),
                                indicatorColor = Color(0xFFE8F5E9)
                            ),
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        // ব্যাক প্রেস করলে যেন বারবার স্ক্রিন স্ট্যাকে জমা না হয়ে ডিরেক্ট হোমে চলে আসে
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // NavHost আমাদের স্ক্রিনগুলোর ট্রাফিক কন্ট্রোল করে
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ১. হোম স্ক্রিন
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onProductClick = { productId ->
                        // প্রোডাক্টে ক্লিক করলে ডিটেইলস স্ক্রিনে নিয়ে যাবে এবং আইডি পাস করবে
                        navController.navigate("details/$productId")
                    }
                )
            }

            // ২. কার্ট স্ক্রিন
            composable(BottomNavItem.Cart.route) {
                // আপাতত ডামি টেক্সট, পরে আমরা এখানে আসল কার্ট পেইজ বসাবো
                CartScreen()
            }

            // ৩. অর্ডার স্ক্রিন
            composable(BottomNavItem.Orders.route) {
                DummyScreen(title = "আপনার কোনো একটিভ অর্ডার নেই।")
            }

            // ৪. প্রোডাক্ট ডিটেইলস স্ক্রিন (বটম বারে থাকবে না, কিন্তু নেভিগেশনে থাকবে)
            composable(
                route = "details/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailsScreen(
                    productId = productId,
                    onBackClick = { navController.navigateUp() } // ব্যাক বাটনে ক্লিক করলে আগের স্ক্রিনে ফেরত যাবে
                )
            }
        }
    }
}

@Composable
fun DummyScreen(title: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    }
}
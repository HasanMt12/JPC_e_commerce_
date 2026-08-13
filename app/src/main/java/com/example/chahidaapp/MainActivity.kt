package com.example.chahidaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chahidaapp.screens.auth.SignUpScreen
import com.example.chahidaapp.screens.cart.CartScreen
import com.example.chahidaapp.screens.details.ProductDetailsScreen
import com.example.chahidaapp.screens.home.HomeScreen
import com.example.chahidaapp.ui.theme.ChahidaAppTheme
import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chahidaapp.screens.cart.CartViewModel
import com.example.chahidaapp.screens.cart.FlyingCartOverlay
import com.example.chahidaapp.screens.cart.FlyingCartState
import com.example.chahidaapp.screens.cart.rememberFlyingCartState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

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

// Bottom navigation item model
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "হোম", Icons.Default.Home)
    object Cart : BottomNavItem("cart", "কার্ট", Icons.Default.ShoppingCart)
    object Orders : BottomNavItem("orders", "অর্ডার", Icons.Default.List)
    object Profile : BottomNavItem("profile", "প্রোফাইল", Icons.Default.Person)
}

@Composable
fun CustomGlassBottomNavigationBar(
    hazeState: HazeState,
    currentRoute: String?,
    cartBadgeCount: Int = 0,
    flyingCartState: FlyingCartState, // 👈 Added flying animation state reference
    onTabSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Orders,
        BottomNavItem.Profile
    )

    val dockShape = RoundedCornerShape(20.dp)
    val brandGreen = Color(0xFF2E7D32)

    val glassModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Modifier.hazeEffect(
            state = hazeState,
            style = HazeStyle(
                tint = HazeTint(Color.White.copy(alpha = 0.35f)),
                blurRadius = 16.dp,
                noiseFactor = 0.02f
            )
        )
    } else {
        Modifier.background(Color.White)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(dockShape)
                .then(glassModifier)
                .background(Color.White.copy(alpha = 0.20f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.40f),
                    shape = dockShape
                )
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val isCartIcon = item.route == BottomNavItem.Cart.route
                val contentColor = if (isSelected) brandGreen else Color.DarkGray.copy(alpha = 0.7f)

                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "iconScale"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTabSelected(item) }
                        .padding(vertical = 4.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (isCartIcon && cartBadgeCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFE53935),
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = cartBadgeCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            if (isCartIcon) {
                                // Stores cart icon's position for flying target destination
                                flyingCartState.cartTargetCoordinates = coordinates
                            }
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = contentColor,
                            modifier = Modifier
                                .size(22.dp)
                                .scale(iconScale)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun MainAppStructure(
    cartViewModel: CartViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val hazeState = HazeState()

    val flyingCartState = rememberFlyingCartState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalCartBadgeCount = cartItems.sumOf { it.quantity }

    FlyingCartOverlay(state = flyingCartState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                if (currentRoute in listOf("home", "cart", "orders", "profile")) {
                    CustomGlassBottomNavigationBar(
                        hazeState = hazeState,
                        currentRoute = currentRoute,
                        cartBadgeCount = totalCartBadgeCount,
                        flyingCartState = flyingCartState,
                        onTabSelected = { item ->
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        ) { _ ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .haze(state = hazeState)
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        onAddToCartWithCoords = { product, coords ->
                            cartViewModel.addToCart(product)
                            flyingCartState.triggerFly(coords)
                        },
                        onProductClick = { productId ->
                            navController.navigate("details/$productId")
                        }
                    )
                }

                composable(BottomNavItem.Cart.route) {
                    CartScreen(cartViewModel = cartViewModel)
                }

                composable(BottomNavItem.Orders.route) {
                    DummyScreen(title = "আপনার কোনো একটিভ অর্ডার নেই।")
                }

                composable(BottomNavItem.Profile.route) {
                    SignUpScreen()
                }

                composable(
                    route = "details/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetailsScreen(
                        productId = productId,
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}
@Composable
fun DummyScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = Color(0xFF2E7D32), style = MaterialTheme.typography.titleMedium)
    }
}
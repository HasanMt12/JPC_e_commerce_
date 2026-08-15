package com.example.chahidaapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.chahidaapp.screens.cart.*
import com.example.chahidaapp.screens.details.ProductDetailsScreen
import com.example.chahidaapp.screens.home.*
import com.example.chahidaapp.screens.products.ProductsScreen
import com.example.chahidaapp.screens.checkout.CheckoutScreen
import com.example.chahidaapp.screens.orders.MyOrdersScreen
import com.example.chahidaapp.ui.theme.ChahidaAppTheme
import dev.chrisbanes.haze.*
import kotlinx.coroutines.launch

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

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "হোম", Icons.Default.Home)
    object Cart : BottomNavItem("cart", "কার্ট", Icons.Default.ShoppingCart)
    object Orders : BottomNavItem("orders", "অর্ডার", Icons.Default.List)
    object Products : BottomNavItem("products_tab", "পণ্য", Icons.Default.AddCircle)
}

@Composable
fun CustomGlassBottomNavigationBar(
    hazeState: HazeState,
    currentRoute: String?,
    cartBadgeCount: Int = 0,
    flyingCartState: FlyingCartState,
    onTabSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Orders,
        BottomNavItem.Products
    )
    val dockShape = RoundedCornerShape(24.dp)
    val primaryGold = Color(0xFFE1A200)
    val darkBg = Color(0xFF171512)

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
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(dockShape)
                .then(glassModifier)
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        tint = HazeTint(darkBg.copy(alpha = 0.2f)),
                        blurRadius = 30.dp,
                        noiseFactor = 0.05f
                    )
                )
                .background(darkBg.copy(alpha = 0.2f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), dockShape)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val isCartIcon = item.route == BottomNavItem.Cart.route

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(item) },
                    contentAlignment = Alignment.Center
                ) {
                    val animatedWidth by animateDpAsState(
                        targetValue = if (isSelected) 100.dp else 0.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "width"
                    )

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .width(animatedWidth)
                                .background(primaryGold.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BadgedBox(
                            badge = {
                                if (isCartIcon && cartBadgeCount > 0) {
                                    Badge(
                                        containerColor = Color(0xFFEC003F),
                                        contentColor = Color.White,
                                        modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                                    ) {
                                        Text(cartBadgeCount.toString(), fontSize = 10.sp)
                                    }
                                }
                            },
                            modifier = Modifier.onGloballyPositioned {
                                if (isCartIcon) flyingCartState.cartTargetCoordinates = it
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.title,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val flyingCartState = rememberFlyingCartState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalCartBadgeCount = cartItems.sumOf { it.quantity }

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Orders,
        BottomNavItem.Products
    )

    val drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
    val darkBg = Color(0xFF171512)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(320.dp)
                    .fillMaxHeight()
                    .clip(drawerShape)
                    // Drawer-এ Haze Glass Effect যোগ করা হয়েছে
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle(
                            tint = HazeTint(darkBg.copy(alpha = 0.25f)),
                            blurRadius = 30.dp,
                            noiseFactor = 0.05f
                        )
                    )
                    .background(darkBg.copy(alpha = 0.25f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), drawerShape),
                drawerContainerColor = Color.Transparent, 
                drawerShape = drawerShape
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    Text("CHAHIDA", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE1A200))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Quality Organic Products", fontSize = 14.sp, color = Color.LightGray)

                    Spacer(modifier = Modifier.weight(1f)) // Moves options to bottom

                    bottomNavItems.forEach { item ->
                        NavigationDrawerItem(
                            label = { Text(item.title, fontWeight = FontWeight.SemiBold) },
                            selected = currentRoute == item.route,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = null) },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFFE1A200).copy(alpha = 0.2f),
                                selectedIconColor = Color(0xFFE1A200),
                                selectedTextColor = Color(0xFFE1A200),
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FlyingCartOverlay(state = flyingCartState) {
                Scaffold(
                    modifier = Modifier.fillMaxSize().statusBarsPadding(),
                    containerColor = Color.Transparent, // কোনো ডিফল্ট ব্যাকগ্রাউন্ড কালার থাকবে না
                    bottomBar = {
                        if (currentRoute in bottomNavItems.map { it.route }) {
                            CustomGlassBottomNavigationBar(
                                hazeState = hazeState,
                                currentRoute = currentRoute,
                                cartBadgeCount = totalCartBadgeCount,
                                flyingCartState = flyingCartState,
                                onTabSelected = { item ->
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        // Only apply top padding to avoid status bar overlap, ignore bottom for floating effect
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                            .hazeSource(hazeState)
                    ) {
                        composable(BottomNavItem.Home.route) {
                            HomeScreen(
                                onAddToCartWithCoords = { p, c -> cartViewModel.addToCart(p); flyingCartState.triggerFly(c) },
                                onProductClick = { navController.navigate("details/$it") },
                                onMenuClick = { scope.launch { drawerState.open() } },
                                onSeeAllClick = { navController.navigate("products") },
                                onCategoryClick = { navController.navigate("products?categoryId=$it") }
                            )
                        }
                        composable(BottomNavItem.Cart.route) {
                            CartScreen(
                                cartViewModel = cartViewModel,
                                onCheckoutClick = { navController.navigate("checkout") }
                            )
                        }
                        composable(BottomNavItem.Orders.route) { MyOrdersScreen() }
                        composable(BottomNavItem.Products.route) {
                            ProductsScreen(onBackClick = { navController.popBackStack() }, onProductClick = { navController.navigate("details/$it") }, onAddToCart = { p, c -> cartViewModel.addToCart(p); flyingCartState.triggerFly(c) })
                        }
                        composable("products") {
                            ProductsScreen(onBackClick = { navController.popBackStack() }, onProductClick = { navController.navigate("details/$it") }, onAddToCart = { p, c -> cartViewModel.addToCart(p); flyingCartState.triggerFly(c) })
                        }
                        composable("products?categoryId={categoryId}", arguments = listOf(navArgument("categoryId") { nullable = true })) { backStackEntry ->
                            ProductsScreen(
                                initialCategoryId = backStackEntry.arguments?.getString("categoryId"),
                                onBackClick = { navController.popBackStack() },
                                onProductClick = { productId -> navController.navigate("details/$productId") },
                                onAddToCart = { p, c -> cartViewModel.addToCart(p); flyingCartState.triggerFly(c) }
                            )
                        }
                        composable("checkout") {
                            CheckoutScreen(
                                items = cartItems.map {
                                    com.example.chahidaapp.data.model.OrderItem(
                                        productId = it.product.id,
                                        variantId = it.product.variants.firstOrNull()?.id ?: "",
                                        quantity = it.quantity,
                                        price = it.product.variants.firstOrNull()?.price ?: 0.0,
                                        productTitle = it.product.title,
                                        productImage = it.product.imageUrl
                                    )
                                },
                                subtotal = cartItems.sumOf { (it.product.variants.firstOrNull()?.price ?: 0.0) * it.quantity },
                                onBackClick = { navController.popBackStack() },
                                onOrderSuccess = { cartViewModel.clearCart(); navController.navigate(BottomNavItem.Orders.route) { popUpTo(BottomNavItem.Home.route) } }
                            )
                        }
                        composable("details/{productId}", arguments = listOf(navArgument("productId") { type = NavType.StringType })) {
                            ProductDetailsScreen(
                                productId = it.arguments?.getString("productId") ?: "",
                                onBackClick = { navController.navigateUp() },
                                onAddToCart = { p -> cartViewModel.addToCart(p) },
                                onBuyNowClick = { p -> cartViewModel.addToCart(p); navController.navigate("checkout") }
                            )
                        }
                    }
                }
            }

            FloatingSocialWidget(modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 100.dp, end = 20.dp))
        }
    }
}

@Composable
fun FloatingSocialWidget(
    modifier: Modifier = Modifier,
    websiteViewModel: WebsiteViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val primaryGold = Color(0xFFE1A200)
    val context = androidx.compose.ui.platform.LocalContext.current
    val websiteInfo by websiteViewModel.websiteInfo.collectAsState()

    val openUrl = { url: String? ->
        if (!url.isNullOrBlank()) {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
            context.startActivity(intent)
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        if (expanded) {
            // Messenger
            FloatingActionButton(
                onClick = { openUrl(websiteInfo?.messengerUrl) },
                containerColor = Color(0xFF0084FF),
                modifier = Modifier.size(48.dp).padding(bottom = 8.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Send, contentDescription = "Messenger", tint = Color.White, modifier = Modifier.size(20.dp))
            }
            // WhatsApp
            FloatingActionButton(
                onClick = {
                    val number = websiteInfo?.whatsappNumber?.replace("+", "")
                    openUrl("https://wa.me/$number")
                },
                containerColor = Color(0xFF25D366),
                modifier = Modifier.size(48.dp).padding(bottom = 8.dp),
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(24.dp))
            }
            // Facebook
            FloatingActionButton(
                onClick = { openUrl(websiteInfo?.facebookUrl) },
                containerColor = Color(0xFF1877F2),
                modifier = Modifier.size(48.dp).padding(bottom = 8.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Face, contentDescription = "Facebook", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = primaryGold,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Call,
                contentDescription = "Social",
                tint = Color.White
            )
        }
    }
}
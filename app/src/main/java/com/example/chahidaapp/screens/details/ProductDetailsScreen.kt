package com.example.chahidaapp.screens.details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.chahidaapp.components.HtmlText
import com.example.chahidaapp.data.model.ProductItem
import com.example.chahidaapp.data.model.ProductVariant
import kotlinx.coroutines.launch

@Composable
fun ProductDetailsScreen(
    productId: String,
    onBackClick: () -> Unit,
    onAddToCart: (ProductItem) -> Unit,
    onBuyNowClick: (ProductItem) -> Unit,
    viewModel: ProductDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    when (val state = uiState) {
        is ProductDetailsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        }
        is ProductDetailsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = Color.Red)
            }
        }
        is ProductDetailsUiState.Success -> {
            ProductDetailsContent(
                product = state.product,
                onBackClick = onBackClick,
                onAddToCart = onAddToCart,
                onBuyNowClick = onBuyNowClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    product: ProductItem,
    onBackClick: () -> Unit,
    onAddToCart: (ProductItem) -> Unit,
    onBuyNowClick: (ProductItem) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedVariant by remember { mutableStateOf(product.variants.firstOrNull()) }
    var isFavorite by remember { mutableStateOf(false) }

    val allImages = remember(product, selectedVariant) {
        val images = mutableListOf<String>()
        selectedVariant?.photo?.let { if (it.isNotEmpty()) images.add(it) }
        if (product.imageUrl.isNotEmpty() && !images.contains(product.imageUrl)) images.add(product.imageUrl)
        product.extraImages.forEach { if (it.isNotEmpty() && !images.contains(it)) images.add(it) }
        images
    }

    val pagerState = rememberPagerState(pageCount = { allImages.size })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp).size(40.dp).background(Color.White.copy(alpha = 0.9f), CircleShape)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }, modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.9f), CircleShape)) {
                        Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = null, tint = if (isFavorite) Color.Red else Color.Black)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = 8.dp, shadowElevation = 20.dp, color = Color.White) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { 
                            onAddToCart(product)
                            scope.launch { snackbarHostState.showSnackbar("${product.title} added to cart") }
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32))
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Cart", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onBuyNowClick(product) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text("Buy Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).background(Color.White)) {
            Box(modifier = Modifier.fillMaxWidth().height(350.dp).background(Color.White), contentAlignment = Alignment.BottomCenter) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                    AsyncImage(model = allImages[page], contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)) {
                    Text(text = product.categoryName, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = product.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "৳${selectedVariant?.price ?: 0.0}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E7D32))
                
                Spacer(modifier = Modifier.height(20.dp))
                if (product.variants.isNotEmpty()) {
                    Text(text = "Variants", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(product.variants) { variant ->
                            val isSelected = selectedVariant?.id == variant.id
                            Surface(
                                modifier = Modifier.clickable { selectedVariant = variant },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Color(0xFF2E7D32) else Color(0xFFF5F6FA)
                            ) {
                                Text(text = variant.name, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = if (isSelected) Color.White else Color.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "About Product", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                HtmlText(htmlDescription = product.description, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

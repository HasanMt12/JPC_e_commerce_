package com.example.chahidaapp.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chahidaapp.components.ProductImage
import com.example.yourappname.data.model.ProductItem

import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onProductClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is HomeUiState.Success -> {
                HomeScreenContent(
                    products = state.products,
                    onProductClick = onProductClick
                )
            }
            is HomeUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.errorMessage, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text(text = "Try Again")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    products: List<ProductItem>,
    onProductClick: (String) -> Unit
) {
    // API responses theke banner features extract list filter setup
    val bannerProducts = products.take(5) // Dynamic sample setup take first 5 product images to roll slides

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section 1: Dynamic Banner Carousel Section (Full width spanning 2 columns layout)
        if (bannerProducts.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                HeroImageBanner(bannerProducts = bannerProducts, onProductClick = onProductClick)
            }
        }

        // Section 2: Header Text Title
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "আমাদের প্রোডাক্ট সমূহ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32), // Organic Green color style
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Section 3: Dynamic Product Listing
        items(products) { product ->
            OrganicProductCard(product = product, onProductClick = onProductClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageBanner(
    bannerProducts: List<ProductItem>,
    onProductClick: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { bannerProducts.size })

    // Automatic auto-scroll interval layout logic state validation
    LaunchedEffect(key1 = pagerState.currentPage) {
        delay(3500) // Scroll dynamics trigger transition slide interval
        val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
        pagerState.animateScrollToPage(nextPage)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val product = bannerProducts[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onProductClick(product.id) }
            ) {
                ProductImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier.fillMaxSize()
                )
                // Dark shade backdrop overlay for readable text labels
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
                Text(
                    text = product.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganicProductCard(
    product: ProductItem,
    onProductClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onProductClick(product.id) },
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = product.categoryName,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.title,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Variants theke base low minimum price filter display text logic mapping
            val basePrice = product.variants.firstOrNull()?.price ?: 0.0
            val unitWeightName = product.variants.firstOrNull()?.name ?: ""

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "৳${basePrice.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100)
                    )
                    Text(
                        text = unitWeightName,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }

                Button(
                    onClick = { onProductClick(product.id) },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(text = "কিনুন", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}
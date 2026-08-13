package com.example.chahidaapp.screens.home

import com.example.chahidaapp.components.HeroImageBanner
import com.example.chahidaapp.components.OrganicProductCard
import com.example.chahidaapp.components.ProductImage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chahidaapp.data.model.ApiCategory
import com.example.chahidaapp.data.model.ProductItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onProductClick: (String) -> Unit,
    onAddToCartWithCoords: (ProductItem, LayoutCoordinates) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🌟 টপবার: মেনু, সার্চ বার এবং হার্ট (Favourite) আইকন
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = { /* Open Menu Drawer */ },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF5F6FA), CircleShape)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("কী খুঁজছেন?", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3F51B5),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF5F6FA),
                    unfocusedContainerColor = Color(0xFFF5F6FA)
                ),
                singleLine = true
            )

            IconButton(
                onClick = { /* Open Favourites Screen */ },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF5F6FA), CircleShape)
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Favourite", tint = Color.Black)
            }
        }

        // মেইন ইউজার ইন্টারফেস স্টেট
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HomeUiState.Success -> {
                    // 🔍 লাইভ সার্চ ফিল্টার লজিক
                    val filteredProducts = remember(searchQuery, state.products) {
                        if (searchQuery.isEmpty()) {
                            state.products
                        } else {
                            state.products.filter { product ->
                                product.title.contains(searchQuery, ignoreCase = true) ||
                                        product.categoryName.contains(searchQuery, ignoreCase = true)
                            }
                        }
                    }

                    HomeScreenContent(
                        products = filteredProducts,
                        categories = state.categories,
                        onProductClick = onProductClick,
                        onAddToCartWithCoords = onAddToCartWithCoords
                    )
                }
                is HomeUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.errorMessage, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadHomeData() }) {
                            Text(text = "Try Again")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    products: List<ProductItem>,
    categories: List<ApiCategory>,
    onProductClick: (String) -> Unit,
    onAddToCartWithCoords: (ProductItem, LayoutCoordinates) -> Unit
) {
    val bannerProducts = products.take(5)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, bottom = 100.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (bannerProducts.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                HeroImageBanner(bannerProducts = bannerProducts, onProductClick = onProductClick)
            }
        }

        // 📂 ডাইনামিক লাইভ ক্যাটাগরি লিস্ট
        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(68.dp)
                                .clickable { /* Category filter logic */ }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF5F6FA)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (category.photo.isNotEmpty()) {
                                    ProductImage(
                                        imageUrl = category.photo,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AddCircle,
                                        contentDescription = category.name,
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Flash Deals for You",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "See All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5)
                )
            }
        }

        items(products) { product ->
            OrganicProductCard(
                product = product,
                onProductClick = onProductClick,
                onAddToCart = { item, coords ->
                    onAddToCartWithCoords(item, coords)
                }
            )
        }
    }
}
package com.example.chahidaapp.screens.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chahidaapp.components.OrganicProductCard
import com.example.chahidaapp.components.shimmerEffect
import com.example.chahidaapp.screens.home.HomeUiState
import com.example.chahidaapp.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    initialCategoryId: String? = null,
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCart: (com.example.chahidaapp.data.model.ProductItem, androidx.compose.ui.layout.LayoutCoordinates) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val primaryGold = Color(0xFFE1A200)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "সব পণ্য",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172B)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF0F172B))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Search Bar (Gold Theme)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("পণ্য খুঁজছেন?") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = primaryGold) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryGold,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFF5F6FA)
                ),
                singleLine = true
            )

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    ProductsScreenLoadingSkeleton()
                }
                is HomeUiState.Success -> {
                    val filteredProducts = state.products.filter { product ->
                        val matchesSearch = product.title.contains(searchQuery, ignoreCase = true) ||
                                product.categoryName.contains(searchQuery, ignoreCase = true)
                        val matchesCategory = initialCategoryId == null || product.categoryId == initialCategoryId
                        matchesSearch && matchesCategory
                    }

                    if (filteredProducts.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "কোনো পণ্য পাওয়া যায়নি", color = Color.Gray, fontWeight = FontWeight.Medium)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredProducts) { product ->
                                OrganicProductCard(
                                    product = product,
                                    onProductClick = onProductClick,
                                    onAddToCart = onAddToCart
                                )
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "ত্রুটি: ${state.errorMessage}", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductsScreenLoadingSkeleton() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
    }
}

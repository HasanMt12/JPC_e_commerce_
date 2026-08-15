package com.example.chahidaapp.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chahidaapp.components.HeroImageBanner
import com.example.chahidaapp.components.OrganicProductCard
import com.example.chahidaapp.components.ProductImage
import com.example.chahidaapp.components.shimmerEffect
import com.example.chahidaapp.data.model.ApiCategory
import com.example.chahidaapp.data.model.Faq
import com.example.chahidaapp.data.model.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    websiteViewModel: WebsiteViewModel = viewModel(),
    onProductClick: (String) -> Unit,
    onAddToCartWithCoords: (ProductItem, LayoutCoordinates) -> Unit,
    onMenuClick: () -> Unit,
    onSeeAllClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val websiteInfo by websiteViewModel.websiteInfo.collectAsState()
    
    val primaryGold = Color(0xFFE1A200)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🌟 Stylish Header (Removed Search Icon from right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF5F6FA), CircleShape)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
            }

            Text(
                text = "Chahida",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = primaryGold,
                letterSpacing = (-1).sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Right side placeholder to keep title centered
            Box(modifier = Modifier.size(44.dp))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    HomeScreenLoadingSkeleton()
                }
                is HomeUiState.Success -> {
                    HomeScreenContent(
                        products = state.products,
                        categories = state.categories.filter { !it.hidden },
                        faqs = state.faqs,
                        onProductClick = onProductClick,
                        onAddToCartWithCoords = onAddToCartWithCoords,
                        onSeeAllClick = onSeeAllClick,
                        onCategoryClick = onCategoryClick
                    )
                }
                is HomeUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.errorMessage, color = Color.Red)
                        Button(onClick = { viewModel.loadHomeData() }, colors = ButtonDefaults.buttonColors(containerColor = primaryGold)) { 
                            Text("Try Again") 
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenLoadingSkeleton() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(24.dp)).shimmerEffect())
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.width(100.dp).height(24.dp).shimmerEffect())
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) {
                Box(modifier = Modifier.size(60.dp).clip(CircleShape).shimmerEffect())
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.width(150.dp).height(24.dp).shimmerEffect())
            Box(modifier = Modifier.width(60.dp).height(24.dp).shimmerEffect())
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.weight(1f).height(250.dp).clip(RoundedCornerShape(16.dp)).shimmerEffect())
            Box(modifier = Modifier.weight(1f).height(250.dp).clip(RoundedCornerShape(16.dp)).shimmerEffect())
        }
    }
}

@Composable
fun HomeScreenContent(
    products: List<ProductItem>,
    categories: List<ApiCategory>,
    faqs: List<Faq>,
    onProductClick: (String) -> Unit,
    onAddToCartWithCoords: (ProductItem, LayoutCoordinates) -> Unit,
    onSeeAllClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val bannerProducts = products.take(5)
    val primaryGold = Color(0xFFE1A200)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, bottom = 120.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (bannerProducts.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                HeroImageBanner(bannerProducts = bannerProducts, onProductClick = onProductClick)
            }
        }

        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                SectionTitle("ক্যাটাগরি")
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categories) { category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(70.dp).clickable { onCategoryClick(category.id) }
                        ) {
                            Box(
                                modifier = Modifier.size(65.dp).clip(CircleShape).background(Color(0xFFF5F6FA)),
                                contentAlignment = Alignment.Center
                            ) {
                                ProductImage(imageUrl = category.photo, modifier = Modifier.fillMaxSize().clip(CircleShape))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = category.name, fontSize = 12.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("আপনার জন্য সেরা ডিল")
                Text("সব দেখুন", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryGold, modifier = Modifier.clickable { onSeeAllClick() })
            }
        }

        items(products) { product ->
            OrganicProductCard(product = product, onProductClick = onProductClick, onAddToCart = onAddToCartWithCoords)
        }

        if (faqs.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle("জিজ্ঞাসিত প্রশ্নাবলী (FAQ)")
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(faqs, span = { GridItemSpan(2) }) { faq ->
                FaqItem(faq)
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF0F172B),
        letterSpacing = (-0.5).sp
    )
}

@Composable
fun FaqItem(faq: Faq) {
    var expanded by remember { mutableStateOf(false) }
    val primaryGold = Color(0xFFE1A200)
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        border = BorderStroke(1.dp, if(expanded) primaryGold else Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).background(primaryGold.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = primaryGold, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = faq.question, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172B))
                Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null, tint = primaryGold)
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = faq.answer, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            }
        }
    }
}

package com.example.chahidaapp.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = viewModel(),
    onCheckoutClick: () -> Unit = {}
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { (it.product.variants.firstOrNull()?.price ?: 0.0) * it.quantity }
    val deliveryCharge = 60.0
    val total = subtotal + deliveryCharge
    
    val primaryGold = Color(0xFFE1A200)
    val darkBg = Color(0xFF171512)
    val priceRed = Color(0xFFEC003F)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "আপনার কার্ট খালি আছে",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "My Cart",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = darkBg,
                    modifier = Modifier.padding(24.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(cartItems, key = { it.product.id }) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { cartViewModel.addToCart(item.product) },
                            onDecrease = { cartViewModel.removeFromCart(item.product) },
                            onDelete = { cartViewModel.deleteCartItem(item.product) }
                        )
                    }
                }
            }

            // Summary Section (Modern Floating Design)
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 20.dp) // Lifted up with margin
                    .padding(bottom = 80.dp) // Extra gap for floating nav
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = primaryGold),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
                        Text("৳${subtotal.toInt()}", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
                        Text("৳${deliveryCharge.toInt()}", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Total", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        Text("৳${total.toInt()}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Button(
                        onClick = onCheckoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = darkBg),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Checkout Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    val darkBg = Color(0xFF171512)
    val priceRed = Color(0xFFEC003F)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F6FA)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = darkBg,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Plus Minus Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFF5F6FA), RoundedCornerShape(12.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = darkBg, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = item.quantity.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = darkBg, modifier = Modifier.size(16.dp))
                }
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "৳${((item.product.variants.firstOrNull()?.price ?: 0.0) * item.quantity).toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = priceRed
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp).padding(top = 4.dp)) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray.copy(alpha = 0.4f), modifier = Modifier.size(18.dp))
            }
        }
    }
}

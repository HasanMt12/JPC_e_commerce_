package com.example.chahidaapp.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    viewModel: OrderViewModel = viewModel()
) {
    val localOrders by viewModel.localOrders.collectAsState()
    val primaryGold = Color(0xFFE1A200)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("আমার অর্ডার সমূহ", fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172B)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
        ) {
            if (localOrders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "আপনার কোনো একটিভ অর্ডার নেই।", color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(localOrders) { order ->
                        OrderCard(order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: com.example.chahidaapp.data.model.OrderData) {
    val primaryGold = Color(0xFFE1A200)
    val priceRed = Color(0xFFEC003F)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val orderId = order.id ?: ""
                    Text(
                        text = "ID: #${if(orderId.length >= 8) orderId.takeLast(8).uppercase() else orderId.uppercase()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0F172B)
                    )
                    Text(
                        text = order.createdAt?.take(10) ?: "",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Surface(
                    color = when(order.status) {
                        "pending" -> Color(0xFFFFF3E0)
                        "completed" -> Color(0xFFE8F5E9)
                        else -> Color(0xFFF5F5F5)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = (order.status ?: "pending").uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when(order.status) {
                            "pending" -> Color(0xFFE65100)
                            "completed" -> Color(0xFF2E7D32)
                            else -> Color.DarkGray
                        }
                    )
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
            
            // Product Items with Photos
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                order.items?.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.productImage,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF5F6FA)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.productTitle ?: "পণ্য",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color(0xFF0F172B)
                            )
                            Text(
                                text = "Qty: ${item.quantity}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "৳${item.price?.toInt() ?: 0}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF0F172B)
                        )
                    }
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "সর্বমোট", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(
                    text = "৳${order.total?.toInt() ?: 0}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = priceRed
                )
            }
        }
    }
}

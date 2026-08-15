package com.example.chahidaapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chahidaapp.data.model.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganicProductCard(
    product: ProductItem,
    onProductClick: (String) -> Unit,
    onAddToCart: (ProductItem, LayoutCoordinates) -> Unit
) {
    var buttonCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val basePrice = product.variants.firstOrNull()?.price ?: 0.0
    val unitWeightName = product.variants.firstOrNull()?.name ?: ""
    val interactionSource = remember { MutableInteractionSource() }

    // Premium Organic Color Palette
    val primaryGold = Color(0xFFE1A200)
    val lightGreenBg = Color(0xFFF1F8F4)
    val cardBorderColor = Color(0xFFE8ECE9)
    val textDark = Color(0xFF171512)
    val textMuted = Color(0xFF6B7280)
    val priceColor = Color(0xFFEC003F)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp,
            focusedElevation = 4.dp
        ),
        border = BorderStroke(1.dp, cardBorderColor),
        onClick = { onProductClick(product.id) },
        modifier = Modifier
            .fillMaxWidth()
            .height(275.dp)
            .padding(2.dp) // Gives shadow room to render beautifully
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 🖼️ Product Image Area with Decorative Badging
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(lightGreenBg),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier.size(115.dp)
                )

                // 🏷️ Premium "Organic" badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            color = Color(0xFFE8F5E9).copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "ORGANIC",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // 📝 Product Information Metadata & Title
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.categoryName.uppercase(),
                    fontSize = 10.sp,
                    color = primaryGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.title,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = textDark,
                    lineHeight = 18.sp
                )
            }

            // 💰 Price, Unit, and Premium Add to Cart Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price and Weight Unit info
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "৳${basePrice.toInt()}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = priceColor
                    )
                    if (unitWeightName.isNotEmpty()) {
                        Text(
                            text = unitWeightName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = textMuted
                        )
                    }
                }

                // 🛒 Gorgeous Premium "Add to Cart" Button
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = primaryGold),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 5.dp
                    ),
                    modifier = Modifier
                        .size(36.dp)
                        .onGloballyPositioned { coordinates ->
                            buttonCoordinates = coordinates
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = LocalIndication.current
                        ) {
                            buttonCoordinates?.let { coords ->
                                onAddToCart(product, coords)
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

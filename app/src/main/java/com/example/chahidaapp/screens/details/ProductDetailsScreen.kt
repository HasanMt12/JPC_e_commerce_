package com.example.chahidaapp.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chahidaapp.components.HtmlText
import com.example.chahidaapp.components.ProductImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    onBackClick: () -> Unit
) {
    // এখানে পরবর্তীতে আমরা ViewModel থেকে আইডি অনুযায়ী রিয়েল ডাটা লোড করব।
    // আপাতত ডিজাইনটি দেখার জন্য আমরা ডামি ডাটা দিয়ে UI সাজাচ্ছি।

    val dummyTitle = "ড্রাই রকমেলন (Dry Rock melon)"
    val dummyCategory = "Dry Food"
    val dummyPrice = 250.0
    val dummyDescription = "<p>রক মেলন এর মিষ্টি স্বাদ ও ঘ্রাণ এখন ড্রাই স্লাইসে। Dry Rock melon ভিটামিন-এ এবং সি-র চমৎকার উৎস, যা দৃষ্টিশক্তি উন্নত করতে সাহায্য করে।</p>"
    val dummyImageUrl = "https://res.cloudinary.com/dj8kkfts1/image/upload/v1777993045/n4qzxlusry3smwzl8lr5.jpg"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("প্রোডাক্ট ডিটেইলস", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF9F9F9))
        ) {
            // প্রোডাক্ট ইমেজ সেকশন
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(
                    imageUrl = dummyImageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // ইনফরমেশন সেকশন
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = dummyCategory,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dummyTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "৳${dummyPrice.toInt()}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "বিস্তারিত বিবরণ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // আমাদের তৈরি করা কাস্টম HtmlText কম্পোনেন্টটি এখানে ব্যবহার করছি
                    HtmlText(
                        htmlDescription = dummyDescription,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // কার্টে যুক্ত করার বাটন
                    Button(
                        onClick = { /* Add to Cart Logic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("কার্টে যুক্ত করুন", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
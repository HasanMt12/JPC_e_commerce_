package com.example.chahidaapp.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.example.chahidaapp.data.model.OrderItem
import com.example.chahidaapp.data.model.OrderRequest
import com.example.chahidaapp.screens.orders.OrderUiState
import com.example.chahidaapp.screens.orders.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    items: List<OrderItem>,
    subtotal: Double,
    onBackClick: () -> Unit,
    onOrderSuccess: () -> Unit,
    viewModel: OrderViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    
    var selectedDivision by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }
    
    var divisionExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }

    val divisions = listOf("Dhaka", "Chittagong", "Rajshahi", "Khulna", "Barisal", "Sylhet", "Rangpur", "Mymensingh")
    val districtsMap = mapOf(
        "Dhaka" to listOf("Dhaka", "Gazipur", "Narayanganj", "Tangail", "Faridpur"),
        "Chittagong" to listOf("Chittagong", "Cox's Bazar", "Cumilla", "Feni", "Noakhali"),
        "Rajshahi" to listOf("Rajshahi", "Bogura", "Pabna", "Naogaon", "Natore"),
        "Khulna" to listOf("Khulna", "Jashore", "Kushtia", "Satkhira", "Bagerhat")
    )

    var deliveryType by remember { mutableStateOf("Home Delivery") }
    val deliveryCharge = 60.0
    val total = subtotal + deliveryCharge
    
    val primaryGold = Color(0xFFE1A200)
    val priceRed = Color(0xFFEC003F)

    val orderState by viewModel.orderState.collectAsState()

    LaunchedEffect(orderState) {
        if (orderState is OrderUiState.Success) {
            onOrderSuccess()
            viewModel.resetOrderState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("চেকআউট", fontWeight = FontWeight.Bold) },
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
                .background(Color(0xFFF8F9FA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 📝 Order Items List
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("অর্ডার আইটেম সমূহ", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color(0xFF0F172B))
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item.productImage,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF5F6FA)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item.productTitle ?: "পণ্য",
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${item.quantity} x ৳${item.price?.toInt() ?: 0}",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Order Summary
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("অর্ডার সামারি", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF0F172B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("সাবটোটাল", color = Color.Gray)
                        Text("৳${subtotal.toInt()}")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ডেলিভারি চার্জ", color = Color.Gray)
                        Text("৳${deliveryCharge.toInt()}")
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("মোট", fontWeight = FontWeight.Bold, color = Color(0xFF0F172B))
                        Text("৳${total.toInt()}", fontWeight = FontWeight.Black, color = priceRed, fontSize = 22.sp)
                    }
                }
            }

            // Customer Details
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("আপনার তথ্য", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF0F172B))
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("আপনার নাম") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryGold)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("ফোন নাম্বার") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryGold)
                    )

                    // Division Dropdown
                    Box {
                        OutlinedTextField(
                            value = selectedDivision,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("বিভাগ") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.clickable { divisionExpanded = true }) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryGold)
                        )
                        DropdownMenu(expanded = divisionExpanded, onDismissRequest = { divisionExpanded = false }) {
                            divisions.forEach { division ->
                                DropdownMenuItem(text = { Text(division) }, onClick = { selectedDivision = division; divisionExpanded = false; selectedDistrict = "" })
                            }
                        }
                    }

                    // District Dropdown
                    Box {
                        OutlinedTextField(
                            value = selectedDistrict,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("জেলা") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.clickable { districtExpanded = true }) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryGold)
                        )
                        DropdownMenu(expanded = districtExpanded, onDismissRequest = { districtExpanded = false }) {
                            districtsMap[selectedDivision]?.forEach { district ->
                                DropdownMenuItem(text = { Text(district) }, onClick = { selectedDistrict = district; districtExpanded = false })
                            } ?: DropdownMenuItem(text = { Text("Select Division First") }, onClick = { districtExpanded = false })
                        }
                    }

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("বিস্তারিত ঠিকানা") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryGold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank() && address.isNotBlank() && selectedDivision.isNotBlank()) {
                        val fullAddress = "$address, $selectedDistrict, $selectedDivision"
                        val request = OrderRequest(
                            customerName = name,
                            customerPhone = phone,
                            customerAddress = fullAddress,
                            deliveryType = deliveryType,
                            deliveryCharge = deliveryCharge,
                            subtotal = subtotal,
                            total = total,
                            items = items
                        )
                        viewModel.placeOrder(request)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGold),
                enabled = orderState !is OrderUiState.Loading
            ) {
                if (orderState is OrderUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("অর্ডার কনফার্ম করুন", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

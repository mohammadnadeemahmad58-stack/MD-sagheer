package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DairyRepository
import com.example.model.BookingOrder
import com.example.model.DeliveryFrequency
import com.example.model.DeliverySlot
import com.example.model.Product
import com.example.ui.theme.DairyGoldContainer
import com.example.ui.theme.DairyGoldPrimary
import com.example.ui.theme.DairyGreenContainer
import com.example.ui.theme.DairyGreenPrimary
import com.example.ui.theme.DairyGreenSecondary
import com.example.ui.theme.DairyOnGoldContainer
import com.example.ui.theme.DairyOnGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerScreen(
    onNavigateToTrack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedProductForBooking by remember { mutableStateOf<Product?>(null) }
    var confirmedOrder by remember { mutableStateOf<BookingOrder?>(null) }

    val categories = listOf("All", "Fresh Milk", "Ghee & Dairy", "Fresh Dairy")
    val filteredProducts = if (selectedCategory == "All") {
        DairyRepository.products
    } else {
        DairyRepository.products.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFA))
    ) {
        // Farm Fresh Banner & Purity Promise
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            colors = CardDefaults.cardColors(containerColor = DairyGreenContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DairyGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Morning Batch Dispatch",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DairyOnGreenContainer
                            )
                            Text(
                                text = "Milked 4:30 AM • Farm to Doorstep",
                                fontSize = 11.sp,
                                color = DairyGreenPrimary
                            )
                        }
                    }

                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.border(1.dp, Color(0xFFC6E7D1), RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "100% Pure & Raw",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DairyGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Pre-book fresh dairy in 30 seconds. We call you directly by phone to confirm before morning delivery.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFF264E36)
                )
            }
        }

        // Category Filter Tabs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DairyGreenPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Product Catalog List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))
            }

            items(filteredProducts) { product ->
                ProductCard(
                    product = product,
                    onBookClick = { selectedProductForBooking = product }
                )
            }

            item {
                // Customer FAQ & Assurance Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = DairyGreenPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "How FreshMilk Direct Works",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1F2937)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. Choose your milk & preferred morning/evening slot.\n2. Submit pre-booking without any online payment.\n3. Dairy owner calls your phone to verify address.\n4. Fresh chilled milk delivered directly to your doorstep. Pay cash or UPI on delivery!",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFF4B5563)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Booking BottomSheet Dialog
    selectedProductForBooking?.let { prod ->
        BookingBottomSheet(
            product = prod,
            onDismiss = { selectedProductForBooking = null },
            onBookingConfirmed = { order ->
                selectedProductForBooking = null
                confirmedOrder = order
            }
        )
    }

    // Booking Success Confirmation Dialog
    confirmedOrder?.let { order ->
        AlertDialog(
            onDismissRequest = { confirmedOrder = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(DairyGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = DairyGreenPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Booking Request Placed!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1F2937)
                )
            },
            text = {
                Column {
                    Surface(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Booking ID", fontSize = 12.sp, color = Color(0xFF6B7280))
                            Text(text = order.id, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DairyGreenPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Thank you, ${order.customerName}! We have received your booking for ${order.quantity} ${order.unit} of ${order.productName}.",
                        fontSize = 13.sp,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = DairyGoldContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = DairyOnGoldContainer, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "The dairy owner will call you at ${order.customerPhone} shortly to confirm your delivery slot!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = DairyOnGoldContainer
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = order.id
                        confirmedOrder = null
                        onNavigateToTrack(id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary)
                ) {
                    Text("Track Live Status")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { confirmedOrder = null }) {
                    Text("Browse More")
                }
            }
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF1F2937)
                        )
                        product.badge?.let { b ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = DairyGoldContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = b,
                                    color = DairyOnGoldContainer,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.tagline,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                // In stock pill
                Surface(
                    color = if (product.inStock) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (product.inStock) "In Stock" else "Sold Out Today",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (product.inStock) Color(0xFF166534) else Color(0xFF991B1B),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fat / SNF / Batch stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = product.fatContent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    color = Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = product.snfContent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    color = DairyGreenContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Milking: ${product.morningBatchTime}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = DairyOnGreenContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = product.description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color(0xFF4B5563)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Price per ${product.unit}",
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₹${product.price.toInt()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DairyGreenPrimary
                        )
                        Text(
                            text = " / ${product.unit}",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Button(
                    onClick = onBookClick,
                    enabled = product.inStock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DairyGreenPrimary,
                        disabledContainerColor = Color(0xFFE5E7EB)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (product.inStock) "Pre-Book" else "Sold Out",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBottomSheet(
    product: Product,
    onDismiss: () -> Unit,
    onBookingConfirmed: (BookingOrder) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var quantity by remember { mutableDoubleStateOf(1.0) }
    var selectedSlot by remember { mutableStateOf(DeliverySlot.MORNING) }
    var selectedFrequency by remember { mutableStateOf(DeliveryFrequency.DAILY) }
    var bottlePreference by remember { mutableStateOf("Sterilized Glass Bottle (Eco)") }
    var deliveryDate by remember { mutableStateOf("Tomorrow Morning") }

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }
    var specialInstructions by remember { mutableStateOf("") }

    var formError by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pre-Book Fresh Delivery",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "${product.name} (₹${product.price.toInt()}/${product.unit})",
                        fontSize = 13.sp,
                        color = DairyGreenPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity Selection
            Text(text = "Select Quantity (${product.unit})", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(10.dp))
                        .padding(4.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 0.5) quantity -= 0.5 },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text(
                        text = "${if (quantity % 1.0 == 0.0) quantity.toInt() else quantity} ${product.unit}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    IconButton(
                        onClick = { quantity += 0.5 },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }

                // Quick presets
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(1.0, 2.0, 3.0, 5.0).forEach { q ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (quantity == q) DairyGreenPrimary else Color(0xFFF3F4F6),
                            modifier = Modifier
                                .clickable { quantity = q }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = "${q.toInt()}L",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (quantity == q) Color.White else Color(0xFF374151),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Frequency
            Text(text = "Delivery Frequency", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DeliveryFrequency.values().forEach { freq ->
                    val isSelected = selectedFrequency == freq
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) DairyGreenContainer else Color(0xFFF9FAFB),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) DairyGreenPrimary else Color(0xFFE5E7EB)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedFrequency = freq }
                    ) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (freq) {
                                    DeliveryFrequency.ONE_TIME -> "One-Time"
                                    DeliveryFrequency.DAILY -> "Daily"
                                    DeliveryFrequency.ALTERNATE_DAYS -> "Alt Days"
                                    DeliveryFrequency.WEEKLY -> "Weekly"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) DairyGreenPrimary else Color(0xFF374151)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Time Slot
            Text(text = "Preferred Delivery Slot", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DeliverySlot.values().forEach { slot ->
                    val isSelected = selectedSlot == slot
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) DairyGreenContainer else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) DairyGreenPrimary else Color(0xFFD1D5DB)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedSlot = slot }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = slot.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) DairyGreenPrimary else Color(0xFF1F2937)
                            )
                            Text(
                                text = slot.timeRange,
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottle preference
            Text(text = "Packaging Type", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Sterilized Glass Bottle (Eco)", "Fresh Sealed Pouch").forEach { pack ->
                    val isSelected = bottlePreference == pack
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) DairyGreenContainer else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) DairyGreenPrimary else Color(0xFFE5E7EB)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { bottlePreference = pack }
                    ) {
                        Text(
                            text = pack,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) DairyGreenPrimary else Color(0xFF4B5563),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFE5E7EB))
            Spacer(modifier = Modifier.height(12.dp))

            // Contact & Delivery Address
            Text(text = "Customer Details (For Verification Call)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Your Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = DairyGreenPrimary) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DairyGreenPrimary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customerPhone,
                onValueChange = { if (it.length <= 15) customerPhone = it },
                label = { Text("Mobile Number (Dairy owner will call here)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = DairyGreenPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DairyGreenPrimary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = deliveryAddress,
                onValueChange = { deliveryAddress = it },
                label = { Text("Complete Delivery Address (House/Flat, Building, Street)") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = DairyGreenPrimary) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DairyGreenPrimary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = landmark,
                onValueChange = { landmark = it },
                label = { Text("Nearby Landmark (e.g. Opposite City Park)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DairyGreenPrimary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = specialInstructions,
                onValueChange = { specialInstructions = it },
                label = { Text("Drop Instructions (e.g. Milk bag on door hook, gate code)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DairyGreenPrimary)
            )

            // Price Summary & Confirmation Call Notice
            Spacer(modifier = Modifier.height(14.dp))
            val totalEstimate = product.price * quantity
            Surface(
                color = DairyGoldContainer,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated Order Total:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = DairyOnGoldContainer
                        )
                        Text(
                            text = "₹${totalEstimate.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DairyOnGoldContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• No online payment needed now.\n• Dairy owner will call your phone to verify details.\n• Pay Cash/UPI directly at delivery.",
                        fontSize = 11.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 16.sp
                    )
                }
            }

            formError?.let { err ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = err, color = Color(0xFFDC2626), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (customerName.isBlank()) {
                        formError = "Please enter your name"
                        return@Button
                    }
                    if (customerPhone.isBlank() || customerPhone.length < 10) {
                        formError = "Please enter a valid 10-digit phone number"
                        return@Button
                    }
                    if (deliveryAddress.isBlank()) {
                        formError = "Please provide your delivery address"
                        return@Button
                    }

                    formError = null
                    val newOrder = DairyRepository.addBooking(
                        customerName = customerName,
                        customerPhone = customerPhone,
                        address = deliveryAddress,
                        landmark = landmark,
                        productId = product.id,
                        quantity = quantity,
                        slot = selectedSlot,
                        frequency = selectedFrequency,
                        deliveryDate = deliveryDate,
                        specialInstructions = specialInstructions,
                        bottlePreference = bottlePreference
                    )
                    onBookingConfirmed(newOrder)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary)
            ) {
                Text(
                    text = "Confirm Booking Request",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

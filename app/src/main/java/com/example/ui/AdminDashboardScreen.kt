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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.BookingStatus
import com.example.model.DeliverySlot
import com.example.model.Product
import com.example.ui.theme.DairyGoldContainer
import com.example.ui.theme.DairyGoldPrimary
import com.example.ui.theme.DairyGreenContainer
import com.example.ui.theme.DairyGreenPrimary
import com.example.ui.theme.DairyOnGoldContainer
import com.example.ui.theme.DairyOnGreenContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Bookings & Calls", "Delivery Route", "Products & Rates", "Customers")

    val allBookings = DairyRepository.bookings
    val pendingCallsCount = allBookings.count { it.status == BookingStatus.PENDING_CALL }
    val totalMorningLitres = allBookings
        .filter { it.status != BookingStatus.CANCELLED && it.slot == DeliverySlot.MORNING }
        .sumOf { it.quantity }
    val totalCashToCollect = allBookings
        .filter { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.OUT_FOR_DELIVERY }
        .sumOf { it.totalAmount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFA))
    ) {
        // Owner Quick Summary Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pending calls card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (pendingCallsCount > 0) DairyGoldContainer else Color(0xFFF3F4F6)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedTab = 0 }
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "To Call",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (pendingCallsCount > 0) DairyOnGoldContainer else Color(0xFF4B5563)
                        )
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = if (pendingCallsCount > 0) DairyGoldPrimary else Color(0xFF6B7280),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$pendingCallsCount Pending",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pendingCallsCount > 0) DairyOnGoldContainer else Color(0xFF1F2937)
                    )
                }
            }

            // Morning Litres Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DairyGreenContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Morning Run",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DairyOnGreenContainer
                        )
                        Icon(
                            Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = DairyGreenPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${if (totalMorningLitres % 1.0 == 0.0) totalMorningLitres.toInt() else totalMorningLitres}L Milk",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyOnGreenContainer
                    )
                }
            }

            // Cash to Collect Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "To Collect",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "₹${totalCashToCollect.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyGreenPrimary
                    )
                }
            }
        }

        // Sub Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = Color.White,
            contentColor = DairyGreenPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                            if (index == 0 && pendingCallsCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = DairyGoldPrimary,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "$pendingCallsCount",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> AdminBookingsTab()
            1 -> AdminDeliveryRouteTab()
            2 -> AdminProductsTab()
            3 -> AdminCustomersTab()
        }
    }
}

@Composable
fun AdminBookingsTab() {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("Needs Call") }
    var searchQuery by remember { mutableStateOf("") }
    var editingOrderNotes by remember { mutableStateOf<BookingOrder?>(null) }

    val allBookings = DairyRepository.bookings
    val pendingCount = allBookings.count { it.status == BookingStatus.PENDING_CALL }

    val filteredList = allBookings.filter { order ->
        val matchesFilter = when (selectedFilter) {
            "Needs Call" -> order.status == BookingStatus.PENDING_CALL
            "Confirmed" -> order.status == BookingStatus.CONFIRMED
            "Out for Delivery" -> order.status == BookingStatus.OUT_FOR_DELIVERY
            "Delivered" -> order.status == BookingStatus.DELIVERED
            else -> true
        }

        val matchesSearch = searchQuery.isBlank() ||
                order.customerName.contains(searchQuery, ignoreCase = true) ||
                order.customerPhone.contains(searchQuery) ||
                order.id.contains(searchQuery, ignoreCase = true) ||
                order.address.contains(searchQuery, ignoreCase = true)

        matchesFilter && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name, phone, order ID, address...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DairyGreenPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DairyGreenPrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val filters = listOf("Needs Call", "Confirmed", "Out for Delivery", "Delivered", "All")
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = if (filter == "Needs Call") "Needs Call ($pendingCount)" else filter,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (filter == "Needs Call") DairyGoldPrimary else DairyGreenPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = DairyGreenPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (selectedFilter == "Needs Call") "All caught up! No pending calls." else "No bookings in this filter",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "New bookings placed by customers will appear here in real-time.",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.id }) { booking ->
                    AdminBookingCard(
                        booking = booking,
                        onCallClick = {
                            DairyRepository.markCustomerCalled(booking.id, "Dialed from Admin app")
                            DairyRepository.makePhoneCall(context, booking.customerPhone)
                        },
                        onWhatsAppClick = {
                            DairyRepository.openWhatsApp(context, booking.customerPhone, booking)
                        },
                        onConfirmBooking = {
                            DairyRepository.updateBookingStatus(booking.id, BookingStatus.CONFIRMED, "Confirmed by dairy owner via phone")
                        },
                        onRejectBooking = {
                            DairyRepository.updateBookingStatus(booking.id, BookingStatus.CANCELLED, "Declined / Out of delivery area")
                        },
                        onMarkOutForDelivery = {
                            DairyRepository.updateBookingStatus(booking.id, BookingStatus.OUT_FOR_DELIVERY, "Packed into crate, out for delivery")
                        },
                        onMarkDelivered = {
                            DairyRepository.updateBookingStatus(booking.id, BookingStatus.DELIVERED, "Delivered to customer doorstep. Cash/UPI received.")
                        },
                        onEditNotes = { editingOrderNotes = booking }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Notes Edit Dialog
    editingOrderNotes?.let { order ->
        var noteText by remember { mutableStateOf(order.adminNotes) }
        AlertDialog(
            onDismissRequest = { editingOrderNotes = null },
            title = { Text("Customer Order Notes (${order.id})", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column {
                    Text("Add internal notes (e.g. customer delivery preferences, payment remarks):", fontSize = 12.sp, color = Color(0xFF6B7280))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DairyGreenPrimary)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        DairyRepository.updateBookingStatus(order.id, order.status, noteText)
                        editingOrderNotes = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary)
                ) {
                    Text("Save Notes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { editingOrderNotes = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminBookingCard(
    booking: BookingOrder,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onConfirmBooking: () -> Unit,
    onRejectBooking: () -> Unit,
    onMarkOutForDelivery: () -> Unit,
    onMarkDelivered: () -> Unit,
    onEditNotes: () -> Unit
) {
    val isPending = booking.status == BookingStatus.PENDING_CALL

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isPending) 2.dp else 1.dp,
                color = if (isPending) DairyGoldPrimary else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isPending) Color(0xFFFFFCF5) else Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID, Date, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = booking.id,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = booking.deliveryDate,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium
                    )
                }
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Contact Action Box (Call-to-confirm workflow)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isPending) DairyGoldContainer else Color(0xFFF9FAFB)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isPending) DairyOnGoldContainer else Color(0xFF374151),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = booking.customerName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isPending) DairyOnGoldContainer else Color(0xFF1F2937)
                            )
                        }
                        Text(
                            text = booking.customerPhone,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DairyGreenPrimary,
                            modifier = Modifier.padding(start = 22.dp)
                        )
                    }

                    // Direct Call & WhatsApp buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onCallClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPending) DairyGoldPrimary else DairyGreenPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onWhatsAppClick,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Order items & pricing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${booking.productName} • ${booking.quantity} ${booking.unit}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Slot: ${booking.slot.displayName} (${booking.slot.timeRange})",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                    Text(
                        text = "Plan: ${booking.frequency.displayName} • Packaging: ${booking.bottlePreference}",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${booking.totalAmount.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyGreenPrimary
                    )
                    Text(
                        text = "Cash on Delivery",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            // Address & Landmark
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = booking.address,
                        fontSize = 12.sp,
                        color = Color(0xFF374151),
                        fontWeight = FontWeight.Medium
                    )
                    if (booking.landmark.isNotBlank()) {
                        Text(
                            text = "Landmark: ${booking.landmark}",
                            fontSize = 11.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }

            if (booking.specialInstructions.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Drop Note: ${booking.specialInstructions}",
                        fontSize = 11.sp,
                        color = Color(0xFF4B5563),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Admin Notes
            if (booking.adminNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditNotes() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Log: ${booking.adminNotes}",
                        fontSize = 11.sp,
                        color = Color(0xFF0369A1),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.Edit, contentDescription = "Edit Notes", modifier = Modifier.size(14.dp), tint = Color(0xFF0369A1))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = Color(0xFFE5E7EB))
            Spacer(modifier = Modifier.height(10.dp))

            // Workflow Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButtonWithIcon(
                    text = "Add Note",
                    icon = Icons.Default.Edit,
                    onClick = onEditNotes
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (booking.status) {
                        BookingStatus.PENDING_CALL -> {
                            OutlinedButton(
                                onClick = onRejectBooking,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Decline", fontSize = 12.sp)
                            }

                            Button(
                                onClick = onConfirmBooking,
                                colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Confirm Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        BookingStatus.CONFIRMED -> {
                            Button(
                                onClick = onMarkOutForDelivery,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Send Out for Delivery", fontSize = 12.sp)
                            }
                        }
                        BookingStatus.OUT_FOR_DELIVERY -> {
                            Button(
                                onClick = onMarkDelivered,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mark Delivered", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        BookingStatus.DELIVERED -> {
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "✓ Completed & Collected",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        BookingStatus.CANCELLED -> {
                            Text(text = "Declined", fontSize = 12.sp, color = Color(0xFF991B1B))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextButtonWithIcon(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF6B7280), modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 12.sp, color = Color(0xFF6B7280))
    }
}

@Composable
fun AdminDeliveryRouteTab() {
    val context = LocalContext.current
    var selectedSlot by remember { mutableStateOf(DeliverySlot.MORNING) }

    val activeDeliveries = DairyRepository.bookings.filter {
        (it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.OUT_FOR_DELIVERY || it.status == BookingStatus.DELIVERED) &&
                it.slot == selectedSlot
    }

    val totalLitres = activeDeliveries.sumOf { it.quantity }
    val completedCount = activeDeliveries.count { it.status == BookingStatus.DELIVERED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Slot Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DeliverySlot.values().forEach { slot ->
                val isSelected = selectedSlot == slot
                Button(
                    onClick = { selectedSlot = slot },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) DairyGreenPrimary else Color(0xFFF3F4F6),
                        contentColor = if (isSelected) Color.White else Color(0xFF374151)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${slot.displayName} (${slot.timeRange.split("–").first().trim()})",
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Run Sheet Summary
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Delivery Route Run-Sheet",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Total Pack: ${if (totalLitres % 1.0 == 0.0) totalLitres.toInt() else totalLitres}L • $completedCount of ${activeDeliveries.size} Drops Completed",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Surface(
                    color = if (completedCount == activeDeliveries.size && activeDeliveries.isNotEmpty()) Color(0xFFDCFCE7) else DairyGreenContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (activeDeliveries.isEmpty()) "No Runs" else "$completedCount/${activeDeliveries.size} Done",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyOnGreenContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeDeliveries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No active deliveries scheduled for this slot.",
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(activeDeliveries.indices.toList(), key = { activeDeliveries[it].id }) { index ->
                    val booking = activeDeliveries[index]
                    val isDone = booking.status == BookingStatus.DELIVERED

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDone) Color(0xFFF9FAFB) else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Stop Number
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isDone) Color(0xFF16A34A) else DairyGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                } else {
                                    Text(
                                        text = "#${index + 1}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Stop details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = booking.customerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isDone) Color(0xFF6B7280) else Color(0xFF1F2937)
                                )
                                Text(
                                    text = "${booking.productName} • ${booking.quantity} ${booking.unit}",
                                    fontSize = 12.sp,
                                    color = DairyGreenPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${booking.address}${if (booking.landmark.isNotBlank()) " (${booking.landmark})" else ""}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280),
                                    lineHeight = 15.sp
                                )
                                if (booking.specialInstructions.isNotBlank()) {
                                    Text(
                                        text = "Note: ${booking.specialInstructions}",
                                        fontSize = 10.sp,
                                        color = Color(0xFFD97706),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Action: Navigation in Maps & Mark Done
                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(
                                    onClick = {
                                        DairyRepository.openMapsNavigation(context, booking.address, booking.landmark)
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Navigation,
                                        contentDescription = "Navigate",
                                        tint = DairyGreenPrimary
                                    )
                                }

                                Checkbox(
                                    checked = isDone,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            DairyRepository.updateBookingStatus(booking.id, BookingStatus.DELIVERED, "Delivered on route")
                                        } else {
                                            DairyRepository.updateBookingStatus(booking.id, BookingStatus.OUT_FOR_DELIVERY, "Delivery pending")
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF16A34A))
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun AdminProductsTab() {
    var editingProductPrice by remember { mutableStateOf<Product?>(null) }
    var isAddingNewProduct by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Catalog & Pricing (${DairyRepository.products.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF374151)
            )

            Button(
                onClick = { isAddingNewProduct = true },
                colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Item", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(DairyRepository.products, key = { it.id }) { product ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1F2937)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = product.category,
                                        fontSize = 10.sp,
                                        color = Color(0xFF4B5563),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "₹${product.price.toInt()} / ${product.unit} • ${product.fatContent}",
                                fontSize = 12.sp,
                                color = DairyGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { editingProductPrice = product },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Price", tint = Color(0xFF6B7280))
                            }

                            Switch(
                                checked = product.inStock,
                                onCheckedChange = {
                                    DairyRepository.toggleProductStock(product.id)
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = DairyGreenPrimary)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Price Update Dialog
    editingProductPrice?.let { prod ->
        var newPriceInput by remember { mutableStateOf("${prod.price.toInt()}") }
        AlertDialog(
            onDismissRequest = { editingProductPrice = null },
            title = { Text("Update Price: ${prod.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column {
                    Text("Enter new price per ${prod.unit} in ₹:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPriceInput,
                        onValueChange = { newPriceInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = newPriceInput.toDoubleOrNull()
                        if (parsed != null && parsed > 0) {
                            DairyRepository.updateProductPrice(prod.id, parsed)
                        }
                        editingProductPrice = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary)
                ) {
                    Text("Save Price")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { editingProductPrice = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Product Dialog
    if (isAddingNewProduct) {
        var name by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Fresh Milk") }
        var price by remember { mutableStateOf("") }
        var unit by remember { mutableStateOf("Litre") }
        var fat by remember { mutableStateOf("6.0% Fat") }
        var tagline by remember { mutableStateOf("Farm fresh pure dairy") }

        AlertDialog(
            onDismissRequest = { isAddingNewProduct = false },
            title = { Text("Add New Dairy Product", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, singleLine = true)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit (e.g. Litre, 500g)") }, singleLine = true)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("Fat/Purity Info") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val p = price.toDoubleOrNull() ?: 50.0
                        if (name.isNotBlank()) {
                            DairyRepository.addProduct(
                                name = name,
                                category = category,
                                price = p,
                                unit = unit,
                                fatContent = fat,
                                snfContent = "Pure Farm Quality",
                                tagline = tagline,
                                description = "Freshly supplied farm milk produced under strict organic practices.",
                                badge = "New Batch"
                            )
                        }
                        isAddingNewProduct = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary)
                ) {
                    Text("Add to Store")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { isAddingNewProduct = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminCustomersTab() {
    val context = LocalContext.current
    val customers = DairyRepository.getCustomers()
    var searchCustomer by remember { mutableStateOf("") }

    val filtered = customers.filter {
        searchCustomer.isBlank() ||
                it.name.contains(searchCustomer, ignoreCase = true) ||
                it.phone.contains(searchCustomer) ||
                it.address.contains(searchCustomer, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchCustomer,
            onValueChange = { searchCustomer = it },
            placeholder = { Text("Search customers by name or phone...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DairyGreenPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DairyGreenPrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Customer Directory (${filtered.size})",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B5563)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered, key = { it.phone }) { cust ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = cust.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = cust.phone,
                                fontSize = 12.sp,
                                color = DairyGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = cust.address,
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    color = DairyGreenContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${cust.totalOrders} Orders (${cust.totalLitres}L)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DairyOnGreenContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    color = Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Spent ₹${cust.totalSpent.toInt()}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF374151),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                DairyRepository.makePhoneCall(context, cust.phone)
                            },
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call Customer", tint = DairyGreenPrimary)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

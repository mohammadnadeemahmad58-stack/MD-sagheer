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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.DairyGoldContainer
import com.example.ui.theme.DairyGreenContainer
import com.example.ui.theme.DairyGreenPrimary
import com.example.ui.theme.DairyOnGoldContainer
import com.example.ui.theme.DairyOnGreenContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomerBookingTracker(
    selectedOrderId: String?,
    onBookNewOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchPhoneQuery by remember { mutableStateOf("") }
    val allBookings = DairyRepository.bookings

    // Filter bookings by phone if entered, or prioritize selectedOrderId / recent
    val displayedBookings = if (searchPhoneQuery.isNotBlank()) {
        allBookings.filter { it.customerPhone.contains(searchPhoneQuery.trim()) }
    } else {
        allBookings
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFA))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Phone Lookup Bar
        OutlinedTextField(
            value = searchPhoneQuery,
            onValueChange = { searchPhoneQuery = it },
            placeholder = { Text("Enter your 10-digit mobile to find your bookings") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DairyGreenPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DairyGreenPrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dairy Helpline Contact Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DairyGreenContainer),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dairy Care & Phone Support",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyOnGreenContainer
                    )
                    Text(
                        text = "Questions about your morning milk drop?",
                        fontSize = 11.sp,
                        color = Color(0xFF2E6B47)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            DairyRepository.makePhoneCall(context, "9876543210")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (displayedBookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No bookings found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B5563)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Check the phone number or place a new fresh milk booking!",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBookNewOrder,
                        colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary)
                    ) {
                        Text("Place New Booking")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "Your Bookings (${displayedBookings.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                }

                items(displayedBookings, key = { it.id }) { booking ->
                    BookingStatusCard(
                        booking = booking,
                        isHighlighted = booking.id == selectedOrderId,
                        onReorder = {
                            DairyRepository.addBooking(
                                customerName = booking.customerName,
                                customerPhone = booking.customerPhone,
                                address = booking.address,
                                landmark = booking.landmark,
                                productId = booking.productId,
                                quantity = booking.quantity,
                                slot = booking.slot,
                                frequency = booking.frequency,
                                deliveryDate = "Tomorrow Morning",
                                specialInstructions = booking.specialInstructions,
                                bottlePreference = booking.bottlePreference
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun BookingStatusCard(
    booking: BookingOrder,
    isHighlighted: Boolean,
    onReorder: () -> Unit
) {
    val context = LocalContext.current
    val formattedDate = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(booking.createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isHighlighted) 2.dp else 1.dp,
                color = if (isHighlighted) DairyGreenPrimary else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Booking ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.id,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyGreenPrimary
                    )
                    Text(
                        text = "Placed $formattedDate",
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live Step Timeline
            StepTimeline(currentStatus = booking.status)

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(12.dp))

            // Order Product & Slot Summary
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
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Slot: ${booking.slot.displayName} (${booking.slot.timeRange})",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                    Text(
                        text = "Plan: ${booking.frequency.displayName} • ${booking.bottlePreference}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${booking.totalAmount.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DairyGreenPrimary
                    )
                    Text(
                        text = "Pay on Delivery",
                        fontSize = 10.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            // Customer address & Landmark
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${booking.address}${if (booking.landmark.isNotBlank()) " (Near ${booking.landmark})" else ""}",
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )
            }

            // Call Status Explanation
            Spacer(modifier = Modifier.height(10.dp))
            when (booking.status) {
                BookingStatus.PENDING_CALL -> {
                    Surface(
                        color = DairyGoldContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = DairyOnGoldContainer, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Awaiting verification call: Dairy owner will ring your mobile (${booking.customerPhone}) to confirm the delivery.",
                                fontSize = 11.sp,
                                color = DairyOnGoldContainer
                            )
                        }
                    }
                }
                BookingStatus.CONFIRMED -> {
                    Surface(
                        color = DairyGreenContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DairyGreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Phone confirmation verified! Chilling and packing fresh morning batch for your doorstep.",
                                fontSize = 11.sp,
                                color = DairyOnGreenContainer
                            )
                        }
                    }
                }
                BookingStatus.OUT_FOR_DELIVERY -> {
                    Surface(
                        color = Color(0xFFE0F2FE),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF0369A1), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Delivery van is on route! Please keep cash/UPI ready or leave milk container at door.",
                                fontSize = 11.sp,
                                color = Color(0xFF0369A1)
                            )
                        }
                    }
                }
                BookingStatus.DELIVERED -> {
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Delivered fresh to your doorstep. Thank you for choosing farm-pure dairy!",
                                fontSize = 11.sp,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                }
                BookingStatus.CANCELLED -> {}
            }

            // Quick Actions: Re-order button & contact dairy button
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        DairyRepository.openWhatsApp(context, "9876543210", booking)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp Dairy", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onReorder,
                    colors = ButtonDefaults.buttonColors(containerColor = DairyGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Re-book", fontSize = 12.sp)
                }
            }
        }
    }
}

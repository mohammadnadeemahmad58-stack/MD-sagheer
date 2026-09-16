package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookingStatus
import com.example.ui.theme.DairyGoldContainer
import com.example.ui.theme.DairyGoldPrimary
import com.example.ui.theme.DairyGreenContainer
import com.example.ui.theme.DairyGreenPrimary
import com.example.ui.theme.DairyOnGoldContainer
import com.example.ui.theme.DairyOnGreenContainer

@Composable
fun StatusBadge(status: BookingStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon) = when (status) {
        BookingStatus.PENDING_CALL -> Triple(
            DairyGoldContainer,
            DairyOnGoldContainer,
            Icons.Default.Phone
        )
        BookingStatus.CONFIRMED -> Triple(
            DairyGreenContainer,
            DairyOnGreenContainer,
            Icons.Default.CheckCircle
        )
        BookingStatus.OUT_FOR_DELIVERY -> Triple(
            Color(0xFFE0F2FE),
            Color(0xFF0369A1),
            Icons.Default.LocalShipping
        )
        BookingStatus.DELIVERED -> Triple(
            Color(0xFFDCFCE7),
            Color(0xFF15803D),
            Icons.Default.Check
        )
        BookingStatus.CANCELLED -> Triple(
            Color(0xFFFEE2E2),
            Color(0xFFB91C1C),
            Icons.Default.Close
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.displayName,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StepTimeline(currentStatus: BookingStatus, modifier: Modifier = Modifier) {
    val steps = listOf(
        Pair("Request Placed", BookingStatus.PENDING_CALL),
        Pair("Call Confirmed", BookingStatus.CONFIRMED),
        Pair("Out for Delivery", BookingStatus.OUT_FOR_DELIVERY),
        Pair("Delivered", BookingStatus.DELIVERED)
    )

    val currentStepIndex = when (currentStatus) {
        BookingStatus.PENDING_CALL -> 0
        BookingStatus.CONFIRMED -> 1
        BookingStatus.OUT_FOR_DELIVERY -> 2
        BookingStatus.DELIVERED -> 3
        BookingStatus.CANCELLED -> -1
    }

    if (currentStatus == BookingStatus.CANCELLED) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFB91C1C))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Booking cancelled or declined. Please place a new booking or call dairy support.",
                    fontSize = 13.sp,
                    color = Color(0xFF991B1B)
                )
            }
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, (label, _) ->
                val isCompleted = index <= currentStepIndex
                val isCurrent = index == currentStepIndex

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> DairyGoldPrimary
                                isCompleted -> DairyGreenPrimary
                                else -> Color(0xFFE5E7EB)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted && !isCurrent) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            color = if (isCompleted) Color.White else Color(0xFF9CA3AF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(
                                if (index < currentStepIndex) DairyGreenPrimary else Color(0xFFE5E7EB)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, (label, _) ->
                val isHighlighted = index <= currentStepIndex
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = if (index == currentStepIndex) FontWeight.Bold else FontWeight.Normal,
                    color = if (isHighlighted) Color(0xFF1F2937) else Color(0xFF9CA3AF),
                    modifier = Modifier.width(72.dp)
                )
            }
        }
    }
}

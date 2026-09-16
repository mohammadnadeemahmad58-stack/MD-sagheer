package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DairyRepository
import com.example.model.BookingStatus
import com.example.ui.AdminDashboardScreen
import com.example.ui.CustomerBookingTracker
import com.example.ui.CustomerScreen
import com.example.ui.theme.DairyGoldContainer
import com.example.ui.theme.DairyGoldPrimary
import com.example.ui.theme.DairyGreenContainer
import com.example.ui.theme.DairyGreenPrimary
import com.example.ui.theme.DairyOnGoldContainer
import com.example.ui.theme.DairyOnGreenContainer
import com.example.ui.theme.MyApplicationTheme

enum class AppRole {
  CUSTOMER,
  ADMIN
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        FreshMilkDirectApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreshMilkDirectApp() {
  var currentRole by remember { mutableStateOf(AppRole.CUSTOMER) }
  var customerNavIndex by remember { mutableIntStateOf(0) }
  var trackOrderId by remember { mutableStateOf<String?>(null) }

  val pendingAdminCalls = DairyRepository.bookings.count { it.status == BookingStatus.PENDING_CALL }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White,
          titleContentColor = Color(0xFF1F2937)
        ),
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(DairyGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "FreshMilk Direct",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = DairyGreenContainer,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = if (currentRole == AppRole.CUSTOMER) "Customer" else "Owner Admin",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DairyOnGreenContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = if (currentRole == AppRole.CUSTOMER) "Farm to Doorstep • Call to Confirm" else "Solo Dairy Operations Hub",
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
              )
            }
          }
        },
        actions = {
          // Role Toggle Pill: Switch between Customer and Admin
          Surface(
            color = if (currentRole == AppRole.ADMIN) DairyGoldContainer else Color(0xFFF3F4F6),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (currentRole == AppRole.ADMIN) DairyGoldPrimary else Color(0xFFE5E7EB)
            ),
            modifier = Modifier
              .padding(end = 12.dp)
              .clickable {
                currentRole = if (currentRole == AppRole.CUSTOMER) AppRole.ADMIN else AppRole.CUSTOMER
              }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = if (currentRole == AppRole.CUSTOMER) Icons.Default.AdminPanelSettings else Icons.Default.Storefront,
                contentDescription = null,
                tint = if (currentRole == AppRole.ADMIN) DairyOnGoldContainer else Color(0xFF374151),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (currentRole == AppRole.CUSTOMER) "Switch to Admin" else "View as Customer",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentRole == AppRole.ADMIN) DairyOnGoldContainer else Color(0xFF374151)
              )
              if (currentRole == AppRole.CUSTOMER && pendingAdminCalls > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                  color = DairyGoldPrimary,
                  shape = CircleShape
                ) {
                  Text(
                    text = "$pendingAdminCalls",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
            }
          }
        }
      )
    },
    bottomBar = {
      if (currentRole == AppRole.CUSTOMER) {
        NavigationBar(
          containerColor = Color.White,
          tonalElevation = 8.dp
        ) {
          NavigationBarItem(
            selected = customerNavIndex == 0,
            onClick = { customerNavIndex = 0 },
            icon = {
              Icon(Icons.Outlined.Storefront, contentDescription = "Products")
            },
            label = { Text("Pre-Book Milk", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = DairyGreenPrimary,
              selectedTextColor = DairyGreenPrimary,
              indicatorColor = DairyGreenContainer
            )
          )

          NavigationBarItem(
            selected = customerNavIndex == 1,
            onClick = { customerNavIndex = 1 },
            icon = {
              BadgedBox(
                badge = {
                  val activeCount = DairyRepository.bookings.count {
                    it.status == BookingStatus.PENDING_CALL || it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.OUT_FOR_DELIVERY
                  }
                  if (activeCount > 0) {
                    Badge(
                      containerColor = DairyGreenPrimary,
                      contentColor = Color.White
                    ) {
                      Text("$activeCount")
                    }
                  }
                }
              ) {
                Icon(Icons.Outlined.ListAlt, contentDescription = "My Orders")
              }
            },
            label = { Text("My Bookings", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = DairyGreenPrimary,
              selectedTextColor = DairyGreenPrimary,
              indicatorColor = DairyGreenContainer
            )
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentRole) {
        AppRole.CUSTOMER -> {
          when (customerNavIndex) {
            0 -> CustomerScreen(
              onNavigateToTrack = { orderId ->
                trackOrderId = orderId
                customerNavIndex = 1
              }
            )
            1 -> CustomerBookingTracker(
              selectedOrderId = trackOrderId,
              onBookNewOrder = { customerNavIndex = 0 }
            )
          }
        }
        AppRole.ADMIN -> {
          AdminDashboardScreen()
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}


package com.example.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.model.BookingOrder
import com.example.model.BookingStatus
import com.example.model.DairyCustomer
import com.example.model.DeliveryFrequency
import com.example.model.DeliverySlot
import com.example.model.Product
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DairyRepository {

    // Initial fresh dairy products
    val products = mutableStateListOf(
        Product(
            id = "prod_buffalo",
            name = "Pure Buffalo Milk",
            category = "Fresh Milk",
            price = 82.0,
            unit = "Litre",
            fatContent = "7.5% Fat",
            snfContent = "9.2% SNF",
            tagline = "Thick, creamy & 100% pure farm milk",
            description = "Natural raw whole buffalo milk sourced directly from healthy Murrah buffaloes. Rich cream layer, perfect for thick tea, homemade malai, and setting rich dahi.",
            inStock = true,
            badge = "Bestseller",
            morningBatchTime = "4:30 AM"
        ),
        Product(
            id = "prod_cow_a2",
            name = "Desi Cow Milk (A2 Gir)",
            category = "Fresh Milk",
            price = 78.0,
            unit = "Litre",
            fatContent = "4.5% Fat",
            snfContent = "8.8% SNF",
            tagline = "A2 Beta-Casein, easy digestion for kids & elders",
            description = "Unprocessed raw milk from grass-fed indigenous Gir cows. Naturally sweet, lightweight on the stomach, free from synthetic hormones or booster injections.",
            inStock = true,
            badge = "A2 Certified",
            morningBatchTime = "4:45 AM"
        ),
        Product(
            id = "prod_cow_fresh",
            name = "Farm Fresh Cow Milk",
            category = "Fresh Milk",
            price = 66.0,
            unit = "Litre",
            fatContent = "3.8% Fat",
            snfContent = "8.5% SNF",
            tagline = "Wholesome daily milk for the whole family",
            description = "Freshly milked and chilled to 4°C within 45 minutes of milking. Delivered in clean sterilized glass bottles or sealed food-grade pouches.",
            inStock = true,
            badge = "Daily Essential",
            morningBatchTime = "5:00 AM"
        ),
        Product(
            id = "prod_bilona_ghee",
            name = "Vedic Bilona Cow Ghee",
            category = "Ghee & Dairy",
            price = 740.0,
            unit = "500ml Jar",
            fatContent = "99.8% Pure",
            snfContent = "Vedic Method",
            tagline = "Traditional wooden churned cultured ghee",
            description = "Prepared by traditional Bilona method from curd made with A2 cow milk. Granular texture (danedar), nutty aroma, loaded with natural antioxidants.",
            inStock = true,
            badge = "Handmade",
            morningBatchTime = "Weekly Batch"
        ),
        Product(
            id = "prod_malai_paneer",
            name = "Fresh Malai Paneer",
            category = "Ghee & Dairy",
            price = 160.0,
            unit = "250g Pack",
            fatContent = "Rich Soft",
            snfContent = "No Starch",
            tagline = "Ultra-soft cottage cheese made every dawn",
            description = "Handcrafted every morning from fresh buffalo milk using natural curdling. Super soft texture that absorbs spices effortlessly without rubbery chew.",
            inStock = true,
            badge = "Fresh Daily",
            morningBatchTime = "5:30 AM"
        ),
        Product(
            id = "prod_organic_dahi",
            name = "Farm Clay-Pot Curd (Dahi)",
            category = "Fresh Dairy",
            price = 55.0,
            unit = "500g Tub",
            fatContent = "Natural Probiotic",
            snfContent = "Thick Set",
            tagline = "Naturally sweet, live probiotic cultures",
            description = "Natural slow-cultured dahi set from boiled fresh whole milk. Smooth, rich and mild tartness, excellent for gut health and digestion.",
            inStock = true,
            badge = "Probiotic",
            morningBatchTime = "Overnight Set"
        ),
        Product(
            id = "prod_masala_chaas",
            name = "Spiced Masala Buttermilk",
            category = "Fresh Dairy",
            price = 30.0,
            unit = "500ml Bottle",
            fatContent = "Low Fat",
            snfContent = "Digestive",
            tagline = "Roasted cumin, rock salt & fresh mint",
            description = "Traditional churned butter-milk blended with stone-ground roasted jeera, rock salt, ginger extract and organic mint. Instant gut refresher.",
            inStock = true,
            badge = "Cooling",
            morningBatchTime = "6:00 AM"
        )
    )

    // Pre-seeded bookings to demonstrate full operational workflow
    val bookings = mutableStateListOf(
        BookingOrder(
            id = "FMD-1085",
            customerName = "Ramesh Sharma",
            customerPhone = "9876543210",
            address = "Flat 402, Green Meadows, Sector 14",
            landmark = "Near City Children Park",
            productId = "prod_buffalo",
            productName = "Pure Buffalo Milk",
            quantity = 2.0,
            unit = "Litre",
            unitPrice = 82.0,
            totalAmount = 164.0,
            slot = DeliverySlot.MORNING,
            frequency = DeliveryFrequency.DAILY,
            deliveryDate = "Tomorrow Morning",
            specialInstructions = "Please call before ringing bell as baby is sleeping. Keep in insulated milk bag.",
            status = BookingStatus.PENDING_CALL,
            createdAt = System.currentTimeMillis() - 15 * 60 * 1000,
            adminNotes = "New customer. Prefers early delivery before 7:00 AM.",
            hasCalledCustomer = false,
            bottlePreference = "Glass Bottle (Refundable ₹50 deposit)"
        ),
        BookingOrder(
            id = "FMD-1084",
            customerName = "Dr. Priya Sundaram",
            customerPhone = "9823456789",
            address = "Villa 12, Palm Grove Enclave",
            landmark = "Behind Apollo Clinic",
            productId = "prod_cow_a2",
            productName = "Desi Cow Milk (A2 Gir)",
            quantity = 1.5,
            unit = "Litre",
            unitPrice = 78.0,
            totalAmount = 117.0,
            slot = DeliverySlot.MORNING,
            frequency = DeliveryFrequency.DAILY,
            deliveryDate = "Tomorrow Morning",
            specialInstructions = "Please leave on doorstep milk tray. Gate code is 4321.",
            status = BookingStatus.CONFIRMED,
            createdAt = System.currentTimeMillis() - 90 * 60 * 1000,
            adminNotes = "Phone confirmation done: verified daily delivery starting tomorrow. Customer very happy with A2 purity test report.",
            hasCalledCustomer = true,
            bottlePreference = "Glass Bottle"
        ),
        BookingOrder(
            id = "FMD-1083",
            customerName = "Vikram Verma",
            customerPhone = "9811223344",
            address = "House 88, Lakeview Residency, Block B",
            landmark = "Opposite Metro Pillar 142",
            productId = "prod_malai_paneer",
            productName = "Fresh Malai Paneer",
            quantity = 2.0,
            unit = "250g Pack",
            unitPrice = 160.0,
            totalAmount = 320.0,
            slot = DeliverySlot.MORNING,
            frequency = DeliveryFrequency.ONE_TIME,
            deliveryDate = "Today Morning",
            specialInstructions = "Needed for family puja lunch. Please deliver fresh morning batch.",
            status = BookingStatus.OUT_FOR_DELIVERY,
            createdAt = System.currentTimeMillis() - 180 * 60 * 1000,
            adminNotes = "Confirmed via phone at 6:15 AM. Packed in chilled crate with delivery boy Sunil.",
            hasCalledCustomer = true,
            bottlePreference = "Food Grade Pouch"
        ),
        BookingOrder(
            id = "FMD-1082",
            customerName = "Ananya Deshmukh",
            customerPhone = "9899001122",
            address = "301, Sunshine Heights, Main Road",
            landmark = "Above HDFC Bank",
            productId = "prod_cow_fresh",
            productName = "Farm Fresh Cow Milk",
            quantity = 1.0,
            unit = "Litre",
            unitPrice = 66.0,
            totalAmount = 66.0,
            slot = DeliverySlot.MORNING,
            frequency = DeliveryFrequency.DAILY,
            deliveryDate = "Today Morning",
            specialInstructions = "Hang on iron gate hook.",
            status = BookingStatus.DELIVERED,
            createdAt = System.currentTimeMillis() - 360 * 60 * 1000,
            adminNotes = "Delivered at 6:40 AM. Cash collected ₹66.",
            hasCalledCustomer = true,
            bottlePreference = "Glass Bottle"
        )
    )

    // Last booking ID created by customer in current session
    var lastCustomerBookingId = mutableStateOf<String?>("FMD-1085")

    fun addBooking(
        customerName: String,
        customerPhone: String,
        address: String,
        landmark: String,
        productId: String,
        quantity: Double,
        slot: DeliverySlot,
        frequency: DeliveryFrequency,
        deliveryDate: String,
        specialInstructions: String,
        bottlePreference: String
    ): BookingOrder {
        val product = products.find { it.id == productId } ?: products.first()
        val nextNum = (bookings.mapNotNull { it.id.replace("FMD-", "").toIntOrNull() }.maxOrNull() ?: 1085) + 1
        val newId = "FMD-$nextNum"
        val total = product.price * quantity

        val newOrder = BookingOrder(
            id = newId,
            customerName = customerName.trim(),
            customerPhone = customerPhone.trim(),
            address = address.trim(),
            landmark = landmark.trim(),
            productId = product.id,
            productName = product.name,
            quantity = quantity,
            unit = product.unit,
            unitPrice = product.price,
            totalAmount = total,
            slot = slot,
            frequency = frequency,
            deliveryDate = deliveryDate,
            specialInstructions = specialInstructions.trim(),
            status = BookingStatus.PENDING_CALL,
            createdAt = System.currentTimeMillis(),
            adminNotes = "Booked via online platform. Awaiting initial confirmation call.",
            hasCalledCustomer = false,
            bottlePreference = bottlePreference
        )

        // Add to top of list
        bookings.add(0, newOrder)
        lastCustomerBookingId.value = newId
        return newOrder
    }

    fun updateBookingStatus(orderId: String, newStatus: BookingStatus, note: String? = null) {
        val index = bookings.indexOfFirst { it.id == orderId }
        if (index != -1) {
            val current = bookings[index]
            val updatedNotes = if (!note.isNullOrBlank()) {
                val timeStamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                if (current.adminNotes.isNotBlank()) "${current.adminNotes}\n[$timeStamp] $note" else "[$timeStamp] $note"
            } else current.adminNotes

            bookings[index] = current.copy(
                status = newStatus,
                adminNotes = updatedNotes,
                hasCalledCustomer = if (newStatus == BookingStatus.CONFIRMED) true else current.hasCalledCustomer
            )
        }
    }

    fun markCustomerCalled(orderId: String, note: String = "Spoke with customer; details verified.") {
        val index = bookings.indexOfFirst { it.id == orderId }
        if (index != -1) {
            val current = bookings[index]
            val timeStamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
            val newNotes = if (current.adminNotes.isNotBlank()) "${current.adminNotes}\n[Called at $timeStamp] $note" else "[Called at $timeStamp] $note"
            bookings[index] = current.copy(
                hasCalledCustomer = true,
                adminNotes = newNotes
            )
        }
    }

    fun updateProductPrice(productId: String, newPrice: Double) {
        val index = products.indexOfFirst { it.id == productId }
        if (index != -1) {
            products[index] = products[index].copy(price = newPrice)
        }
    }

    fun toggleProductStock(productId: String) {
        val index = products.indexOfFirst { it.id == productId }
        if (index != -1) {
            products[index] = products[index].copy(inStock = !products[index].inStock)
        }
    }

    fun addProduct(
        name: String,
        category: String,
        price: Double,
        unit: String,
        fatContent: String,
        snfContent: String,
        tagline: String,
        description: String,
        badge: String?
    ) {
        val id = "prod_" + System.currentTimeMillis()
        products.add(
            Product(
                id = id,
                name = name,
                category = category,
                price = price,
                unit = unit,
                fatContent = fatContent,
                snfContent = snfContent,
                tagline = tagline,
                description = description,
                inStock = true,
                badge = badge
            )
        )
    }

    fun getCustomers(): List<DairyCustomer> {
        val grouped = bookings.groupBy { it.customerPhone }
        return grouped.map { (phone, orders) ->
            val first = orders.first()
            val totalLitres = orders.sumOf { it.quantity }
            val totalSpent = orders.sumOf { it.totalAmount }
            val lastOrder = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date(orders.maxOf { it.createdAt }))
            DairyCustomer(
                phone = phone,
                name = first.customerName,
                address = first.address,
                landmark = first.landmark,
                totalOrders = orders.size,
                totalLitres = totalLitres,
                totalSpent = totalSpent,
                lastOrderDate = lastOrder,
                notes = if (orders.any { it.status == BookingStatus.PENDING_CALL }) "Has pending booking call" else "Verified regular patron"
            )
        }
    }

    // Call customer via dialer
    fun makePhoneCall(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to launch dialer for $phone", Toast.LENGTH_SHORT).show()
        }
    }

    // Compose WhatsApp message with order details
    fun openWhatsApp(context: Context, phone: String, order: BookingOrder) {
        try {
            val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
            val formattedPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
            val text = "Namaste ${order.customerName}! 🥛\nThis is FreshMilk Direct confirming your milk booking (${order.id}).\n\nProduct: ${order.productName} (${order.quantity} ${order.unit})\nSlot: ${order.slot.displayName} (${order.slot.timeRange})\nFrequency: ${order.frequency.displayName}\nAddress: ${order.address}\nTotal Amount: ₹${order.totalAmount.toInt()} (Pay on delivery)\n\nIs this order confirmed for tomorrow's fresh batch delivery? Thank you!"
            val encoded = URLEncoder.encode(text, "UTF-8")
            val url = "https://api.whatsapp.com/send?phone=$formattedPhone&text=$encoded"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not available or link error", Toast.LENGTH_SHORT).show()
        }
    }

    // Open Google Maps navigation for delivery address
    fun openMapsNavigation(context: Context, address: String, landmark: String) {
        try {
            val query = if (landmark.isNotBlank()) "$address, $landmark" else address
            val encoded = URLEncoder.encode(query, "UTF-8")
            val mapUri = Uri.parse("geo:0,0?q=$encoded")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open map app", Toast.LENGTH_SHORT).show()
        }
    }
}

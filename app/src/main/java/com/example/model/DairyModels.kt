package com.example.model

enum class BookingStatus(val displayName: String) {
    PENDING_CALL("Pending Call"),
    CONFIRMED("Confirmed"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

enum class DeliverySlot(val displayName: String, val timeRange: String) {
    MORNING("Early Morning", "6:00 AM – 8:00 AM"),
    EVENING("Evening", "5:00 PM – 7:00 PM")
}

enum class DeliveryFrequency(val displayName: String, val description: String) {
    ONE_TIME("One-Time Trial", "Single fresh delivery to taste purity"),
    DAILY("Daily Subscription", "Delivered every morning at your door"),
    ALTERNATE_DAYS("Alternate Days", "Mon / Wed / Fri / Sun delivery"),
    WEEKLY("Weekly", "Every Sunday special delivery")
}

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val unit: String,
    val fatContent: String,
    val snfContent: String,
    val tagline: String,
    val description: String,
    val inStock: Boolean = true,
    val badge: String? = null,
    val morningBatchTime: String = "4:30 AM"
)

data class BookingOrder(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val address: String,
    val landmark: String = "",
    val productId: String,
    val productName: String,
    val quantity: Double,
    val unit: String,
    val unitPrice: Double,
    val totalAmount: Double,
    val slot: DeliverySlot,
    val frequency: DeliveryFrequency,
    val deliveryDate: String,
    val specialInstructions: String = "",
    val status: BookingStatus = BookingStatus.PENDING_CALL,
    val createdAt: Long = System.currentTimeMillis(),
    val adminNotes: String = "",
    val hasCalledCustomer: Boolean = false,
    val bottlePreference: String = "Glass Bottle (Refundable)"
)

data class DairyCustomer(
    val phone: String,
    val name: String,
    val address: String,
    val landmark: String,
    val totalOrders: Int,
    val totalLitres: Double,
    val totalSpent: Double,
    val lastOrderDate: String,
    val notes: String = ""
)

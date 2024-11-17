package com.example.coastshinerssales.models.response

data class MyOrdersResponse(
    val items: List<Item>,
    val orderDate: String,
    val orderNumber: String,
    val status: String,
    val totalAmount: Double,
    val userEmail: String,
    val userName: String,
    val userPhone: String
)
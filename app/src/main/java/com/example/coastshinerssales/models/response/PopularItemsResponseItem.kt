package com.example.coastshinerssales.models.response

data class PopularItemsResponseItem(
    val __v: Int,
    val _id: String,
    val code: String,
    val createdAt: String,
    val description: String,
    val image: String,
    val isVisible: Boolean,
    val name: String,
    val price: Int,
    val quantity: Int,
    val updatedAt: String
)
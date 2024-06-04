package com.example.prm1.model

import java.text.SimpleDateFormat
import java.util.Date

data class Product(
    val id: Int,
    val name: String,
    val count: String?,
    val date: String,
    val category: String
) {
    fun isExpired(): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = Date()
        val expiryDate = dateFormat.parse(date)
        return expiryDate.before(currentDate)
    }
}

package com.example.calculator.domain.model

data class HistoryItem(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
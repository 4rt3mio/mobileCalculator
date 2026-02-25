package com.example.calculator.data.model

import com.google.firebase.Timestamp

data class HistoryItem(
    val id: String = "",
    val expression: String = "",
    val result: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
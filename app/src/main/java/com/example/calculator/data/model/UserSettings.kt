package com.example.calculator.data.model

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

data class UserSettings(
    val userId: String = "default",
    val theme: AppTheme = AppTheme.SYSTEM,
    val updatedAt: Long = System.currentTimeMillis()
)
package com.example.calculator.domain.repository

import com.example.calculator.domain.model.HistoryItem
import com.example.calculator.domain.model.UserTheme

interface CalculatorRepository {
    suspend fun saveHistory(historyItem: HistoryItem)
    suspend fun getHistory(): List<HistoryItem>
    suspend fun clearHistory()
    suspend fun saveTheme(theme: UserTheme)
    suspend fun getTheme(): UserTheme
}
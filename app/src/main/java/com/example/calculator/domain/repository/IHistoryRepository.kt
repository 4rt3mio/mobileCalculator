package com.example.calculator.domain.repository

import com.example.calculator.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface IHistoryRepository {
    suspend fun addHistoryItem(expression: String, result: String)
    fun getHistoryItems(limit: Long = 20): Flow<List<HistoryItem>>
    suspend fun clearHistory()
}
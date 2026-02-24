package com.example.calculator.data.repository

import com.example.calculator.domain.model.HistoryItem
import com.example.calculator.domain.model.UserTheme
import com.example.calculator.domain.repository.CalculatorRepository

class CalculatorRepositoryImpl : CalculatorRepository {
    private val history = mutableListOf<HistoryItem>()
    private var currentTheme = UserTheme.SYSTEM

    override suspend fun saveHistory(historyItem: HistoryItem) {
        history.add(historyItem)
    }

    override suspend fun getHistory(): List<HistoryItem> = history

    override suspend fun clearHistory() {
        history.clear()
    }

    override suspend fun saveTheme(theme: UserTheme) {
        currentTheme = theme
    }

    override suspend fun getTheme(): UserTheme = currentTheme
}
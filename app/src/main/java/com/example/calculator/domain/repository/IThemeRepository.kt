package com.example.calculator.domain.repository

import com.example.calculator.data.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface IThemeRepository {
    suspend fun saveTheme(theme: AppTheme)
    fun getTheme(): Flow<AppTheme?>
}
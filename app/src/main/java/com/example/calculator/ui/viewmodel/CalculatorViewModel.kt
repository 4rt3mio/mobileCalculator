package com.example.calculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.data.firebase.HistoryRepository
import com.example.calculator.data.firebase.ThemeRepository
import com.example.calculator.data.model.AppTheme
import com.example.calculator.data.model.HistoryItem
import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.domain.usecase.CalculatorUseCase
import com.example.calculator.domain.repository.IHistoryRepository
import com.example.calculator.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class CalculatorViewModel(
    private val useCase: CalculatorUseCase,
    private val historyRepository: IHistoryRepository = HistoryRepository(),
    private val themeRepository: IThemeRepository = ThemeRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val _currentTheme = MutableStateFlow(AppTheme.SYSTEM)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    init {
        observeTheme()
        observeHistory()
    }

    fun onAction(action: String) {
        val oldInput = _state.value.input // сохраняем выражение до обработки
        val newState = useCase.processAction(_state.value, action)
        if (action == "=" && newState.isResult && !newState.result.equals("Error", ignoreCase = true)) {
            saveHistory(oldInput, newState.result)
        }
        _state.value = newState
    }

    fun onShake() {
        onAction("C")
    }

    fun changeTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.saveTheme(theme)
        }
    }

    private fun observeTheme() {
        viewModelScope.launch {
            themeRepository.getTheme().collect { theme ->
                theme?.let { _currentTheme.value = it }
            }
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            historyRepository.getHistoryItems(limit = 20).collect { items ->
                _history.value = items
                Log.d("CalculatorVM", "History loaded: ${items.size} items")
            }
        }
    }

    private fun saveHistory(expression: String, result: String) {
        viewModelScope.launch {
            historyRepository.addHistoryItem(expression, result)
        }
    }

    fun loadAllHistory() {
        viewModelScope.launch {
            historyRepository.getHistoryItems(limit = 1000).collect { items ->
                _history.value = items
                Log.d("CalculatorVM", "All history loaded: ${items.size} items")
            }
        }
    }

    fun loadRecentHistory(limit: Int = 10) {
        viewModelScope.launch {
            historyRepository.getHistoryItems(limit = limit.toLong()).collect { items ->
                _history.value = items
                Log.d("CalculatorVM", "Recent $limit history loaded: ${items.size} items")
            }
        }
    }

    fun clearHistory() {
        _history.value = emptyList()
        Log.d("CalculatorVM", "History cleared locally")
    }

    fun useHistoryExpression(expression: String) {
        Log.d("CalculatorVM", "useHistoryExpression: '$expression'")
        _state.value = _state.value.copy(input = expression, result = "0", isResult = false)
    }
}
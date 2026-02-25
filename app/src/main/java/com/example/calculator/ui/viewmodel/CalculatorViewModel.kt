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
        val newState = useCase.processAction(_state.value, action)
        _state.value = newState
        if (action == "=" && newState.isResult && !newState.result.equals("Error", ignoreCase = true)) {
            saveHistory(_state.value.input, newState.result)
        }
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
            historyRepository.getHistoryItems().collect { items ->
                _history.value = items
            }
        }
    }

    private fun saveHistory(expression: String, result: String) {
        viewModelScope.launch {
            historyRepository.addHistoryItem(expression, result)
        }
    }
}
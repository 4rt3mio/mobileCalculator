package com.example.calculator.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.domain.usecase.CalculatorUseCase

class CalculatorViewModel(
    private val useCase: CalculatorUseCase
) : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: String) {
        state = useCase.processAction(state, action)
    }
}
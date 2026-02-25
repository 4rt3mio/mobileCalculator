package com.example.calculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.domain.usecase.CalculatorUseCase

class CalculatorViewModelFactory(
    private val useCase: CalculatorUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculatorViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
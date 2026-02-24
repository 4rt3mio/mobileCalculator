package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.domain.usecase.CalculatorUseCase
import com.example.calculator.presentation.CalculatorScreen
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val useCase = remember { CalculatorUseCase() }
                var state by remember { mutableStateOf(CalculatorState()) }

                CalculatorScreen(
                    state = state,
                    onAction = { action ->
                        state = useCase.processAction(state, action)
                    }
                )
            }
        }
    }
}
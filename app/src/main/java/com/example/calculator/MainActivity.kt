package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.domain.usecase.CalculatorUseCase
import com.example.calculator.ui.CalculatorScreen
import com.example.calculator.ui.viewmodel.CalculatorViewModel
import com.example.calculator.ui.viewmodel.CalculatorViewModelFactory
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val viewModel: CalculatorViewModel = viewModel(
                    factory = CalculatorViewModelFactory(CalculatorUseCase())
                )
                CalculatorScreen(
                    state = viewModel.state,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}
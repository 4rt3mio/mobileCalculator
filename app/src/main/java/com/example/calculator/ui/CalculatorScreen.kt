package com.example.calculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.ui.components.CalculatorDisplay
import com.example.calculator.ui.components.CalculatorKeyboard

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalculatorDisplay(
            state = state,
            modifier = Modifier.weight(1f)
        )
        CalculatorKeyboard(onButtonClick = onAction)
    }
}
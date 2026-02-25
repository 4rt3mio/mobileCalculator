package com.example.calculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculator.data.model.AppTheme
import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.ui.components.CalculatorDisplay
import com.example.calculator.ui.components.CalculatorKeyboard
import com.example.calculator.ui.theme.ThemeSwitcher

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (String) -> Unit,
    onOpenHistory: () -> Unit,
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onOpenHistory) {
                Text("📋")
            }
            ThemeSwitcher(
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected
            )
        }

        CalculatorDisplay(
            state = state,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalculatorKeyboard(onButtonClick = onAction)
    }
}
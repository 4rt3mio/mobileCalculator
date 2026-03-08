package com.example.calculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    onThemeSelected: (AppTheme) -> Unit,
    onOpenSettings: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

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
            Row {
                ThemeSwitcher(
                    currentTheme = currentTheme,
                    onThemeSelected = onThemeSelected
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onOpenSettings) {
                    Text("⚙️")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CalculatorDisplay(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isLandscape) 80.dp else 120.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        CalculatorKeyboard(
            onButtonClick = onAction,
            modifier = Modifier.weight(1f),
            isLandscape = isLandscape
        )
    }
}
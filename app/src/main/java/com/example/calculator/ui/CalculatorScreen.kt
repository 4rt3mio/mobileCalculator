package com.example.calculator.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.domain.model.CalculatorState

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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (state.input.isNotEmpty()) state.input else state.result,
                    fontSize = 36.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorRow(
                buttons = listOf("C", "±", "%", "/"),
                onButtonClick = onAction
            )
            CalculatorRow(
                buttons = listOf("7", "8", "9", "*"),
                onButtonClick = onAction
            )
            CalculatorRow(
                buttons = listOf("4", "5", "6", "-"),
                onButtonClick = onAction
            )
            CalculatorRow(
                buttons = listOf("1", "2", "3", "+"),
                onButtonClick = onAction
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(2f),
                    onClick = { onAction("0") }
                )
                CalculatorButton(
                    text = ".",
                    modifier = Modifier.weight(1f),
                    onClick = { onAction(".") }
                )
                CalculatorButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    onClick = { onAction("=") }
                )
            }
        }
    }
}

@Composable
fun CalculatorRow(buttons: List<String>, onButtonClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttons.forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier.weight(1f),
                onClick = { onButtonClick(text) }
            )
        }
    }
}

@Composable
fun CalculatorButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (text in listOf("C", "±", "%", "/", "*", "-", "+", "="))
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
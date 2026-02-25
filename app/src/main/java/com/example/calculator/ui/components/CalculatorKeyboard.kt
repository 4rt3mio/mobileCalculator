package com.example.calculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorKeyboard(
    onButtonClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CalculatorRow(
            buttons = listOf("x²", "√", "1/x", "/"),
            onButtonClick = onButtonClick
        )
        CalculatorRow(
            buttons = listOf("C", "⌫", "±", "*"),
            onButtonClick = onButtonClick
        )
        CalculatorRow(
            buttons = listOf("7", "8", "9", "-"),
            onButtonClick = onButtonClick
        )
        CalculatorRow(
            buttons = listOf("4", "5", "6", "+"),
            onButtonClick = onButtonClick
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(text = "1", modifier = Modifier.weight(1f), onClick = { onButtonClick("1") })
                    CalculatorButton(text = "2", modifier = Modifier.weight(1f), onClick = { onButtonClick("2") })
                    CalculatorButton(text = "3", modifier = Modifier.weight(1f), onClick = { onButtonClick("3") })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(text = "0", modifier = Modifier.weight(1f), onClick = { onButtonClick("0") })
                    CalculatorButton(text = ".", modifier = Modifier.weight(1f), onClick = { onButtonClick(".") })
                    CalculatorButton(text = "%", modifier = Modifier.weight(1f), onClick = { onButtonClick("%") })
                }
            }
            CalculatorButton(
                text = "=",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { onButtonClick("=") }
            )
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
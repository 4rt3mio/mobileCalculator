package com.example.calculator.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fontSize: TextUnit = 24.sp
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (text in listOf("C", "⌫", "±", "%", "/", "*", "-", "+", "=", "x²", "√", "1/x"))
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
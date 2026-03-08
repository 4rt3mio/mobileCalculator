package com.example.calculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorKeyboard(
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false
) {
    val horizontalSpacing = if (isLandscape) 4.dp else 8.dp
    val verticalSpacing = if (isLandscape) 4.dp else 8.dp
    val buttonAspectRatio = if (isLandscape) 1.2f else 1.5f
    val buttonFontSize = if (isLandscape) 16.sp else 24.sp
    val smallFontSize = if (isLandscape) 14.sp else 18.sp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        CalculatorRow(
            buttons = listOf("x²", "√", "1/x", "/"),
            onButtonClick = onButtonClick,
            horizontalSpacing = horizontalSpacing,
            buttonAspectRatio = buttonAspectRatio,
            fontSize = { text -> if (text == "1/x" || text == "x²" || text == "√") smallFontSize else buttonFontSize }
        )
        CalculatorRow(
            buttons = listOf("C", "⌫", "±", "*"),
            onButtonClick = onButtonClick,
            horizontalSpacing = horizontalSpacing,
            buttonAspectRatio = buttonAspectRatio,
            fontSize = { text -> if (text == "C" || text == "⌫") smallFontSize else buttonFontSize }
        )
        CalculatorRow(
            buttons = listOf("7", "8", "9", "-"),
            onButtonClick = onButtonClick,
            horizontalSpacing = horizontalSpacing,
            buttonAspectRatio = buttonAspectRatio,
            fontSize = { buttonFontSize }
        )
        CalculatorRow(
            buttons = listOf("4", "5", "6", "+"),
            onButtonClick = onButtonClick,
            horizontalSpacing = horizontalSpacing,
            buttonAspectRatio = buttonAspectRatio,
            fontSize = { buttonFontSize }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(verticalSpacing)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
                ) {
                    listOf("1", "2", "3").forEach { text ->
                        CalculatorButton(
                            text = text,
                            modifier = Modifier.weight(1f).aspectRatio(buttonAspectRatio),
                            onClick = { onButtonClick(text) },
                            fontSize = buttonFontSize
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
                ) {
                    listOf("0", ".", "%").forEach { text ->
                        CalculatorButton(
                            text = text,
                            modifier = Modifier.weight(1f).aspectRatio(buttonAspectRatio),
                            onClick = { onButtonClick(text) },
                            fontSize = buttonFontSize
                        )
                    }
                }
            }
            CalculatorButton(
                text = "=",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { onButtonClick("=") },
                fontSize = if (isLandscape) 20.sp else 24.sp
            )
        }
    }
}

@Composable
fun CalculatorRow(
    buttons: List<String>,
    onButtonClick: (String) -> Unit,
    horizontalSpacing: androidx.compose.ui.unit.Dp,
    buttonAspectRatio: Float,
    fontSize: (String) -> androidx.compose.ui.unit.TextUnit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
    ) {
        buttons.forEach { text ->
            CalculatorButton(
                text = text,
                modifier = Modifier.weight(1f).aspectRatio(buttonAspectRatio),
                onClick = { onButtonClick(text) },
                fontSize = fontSize(text)
            )
        }
    }
}
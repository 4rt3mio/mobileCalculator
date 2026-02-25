package com.example.calculator.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.calculator.data.model.AppTheme

@Composable
fun ThemeSwitcher(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true }
        ) {
            Text("Theme: ${currentTheme.name}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("System") },
                onClick = {
                    onThemeSelected(AppTheme.SYSTEM)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Light") },
                onClick = {
                    onThemeSelected(AppTheme.LIGHT)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Dark") },
                onClick = {
                    onThemeSelected(AppTheme.DARK)
                    expanded = false
                }
            )
        }
    }
}
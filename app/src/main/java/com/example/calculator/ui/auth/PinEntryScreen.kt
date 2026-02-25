package com.example.calculator.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PinEntryScreen(
    viewModel: AuthViewModel,
    onUnlocked: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val biometricAvailable by viewModel.biometricAvailable.collectAsState()
    val PasswordVisualTransformation = VisualTransformation { text ->
        val masked = "•".repeat(text.text.length)
        TransformedText(AnnotatedString(masked), OffsetMapping.Identity)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter PIN", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 6) pin = it },
            label = { Text("PIN") },
            visualTransformation = PasswordVisualTransformation,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                viewModel.verifyPin(pin)
                if (viewModel.isAuthenticated.value) onUnlocked()
            },
            enabled = pin.isNotEmpty()
        ) {
            Text("Unlock")
        }

        if (biometricAvailable) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.authenticateWithBiometrics()
                }
            ) {
                Text("Use Biometrics")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { viewModel.resetPin() }
        ) {
            Text("Forgot PIN?")
        }
    }
}
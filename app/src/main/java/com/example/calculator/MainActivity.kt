package com.example.calculator

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import com.example.calculator.ui.history.HistoryScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.calculator.ui.auth.AuthViewModel
import com.example.calculator.ui.auth.PinEntryScreen
import com.example.calculator.ui.auth.PinSetupScreen
import com.example.calculator.ui.settings.SettingsScreen
import com.example.calculator.ui.CalculatorScreen
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.ui.viewmodel.CalculatorViewModel
import com.example.calculator.ui.viewmodel.CalculatorViewModelFactory
import com.example.calculator.domain.usecase.CalculatorUseCase
import com.example.calculator.utils.ShakeDetector
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculator.data.model.AppTheme
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.rememberSaveable


class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            authViewModel.setActivity(this)

            val calculatorViewModel: CalculatorViewModel = viewModel(
                factory = CalculatorViewModelFactory(CalculatorUseCase())
            )

            val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()
            val isPinSet by authViewModel.isPinSet.collectAsStateWithLifecycle()
            val theme by calculatorViewModel.currentTheme.collectAsStateWithLifecycle()
            val state by calculatorViewModel.state.collectAsStateWithLifecycle()
            val history by calculatorViewModel.history.collectAsStateWithLifecycle()

            val darkTheme = when (theme) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            var showSettings by rememberSaveable { mutableStateOf(false) }
            var showHistory by rememberSaveable { mutableStateOf(false) }

            shakeDetector = ShakeDetector {
                calculatorViewModel.onShake()
            }

            CalculatorTheme(darkTheme = darkTheme) {
                when {
                    showSettings -> {
                        SettingsScreen(
                            onResetPin = {
                                authViewModel.resetPin()
                                showSettings = false
                            }
                        )
                    }
                    !isPinSet -> {
                        PinSetupScreen(
                            viewModel = authViewModel,
                            onPinSet = { /*  */ }
                        )
                    }
                    !isAuthenticated -> {
                        PinEntryScreen(
                            viewModel = authViewModel,
                            onUnlocked = { /*  */ }
                        )
                    }
                    else -> {
                        if (showHistory) {
                            HistoryScreen(
                                historyItems = history,
                                onBack = { showHistory = false },
                                onItemClick = { expression ->
                                    calculatorViewModel.useHistoryExpression(expression)
                                    showHistory = false
                                },
                                onClearHistory = { calculatorViewModel.clearHistory() },
                                onLoadAll = { calculatorViewModel.loadAllHistory() },
                                onLoadRecent = { limit -> calculatorViewModel.loadRecentHistory(limit) }
                            )
                        } else {
                            CalculatorScreen(
                                state = state,
                                onAction = calculatorViewModel::onAction,
                                onOpenHistory = { showHistory = true },
                                currentTheme = theme,
                                onThemeSelected = calculatorViewModel::changeTheme,
                                onOpenSettings = { showSettings = true }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector?.let {
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
                sensorManager.registerListener(it, accelerometer, SensorManager.SENSOR_DELAY_UI) // обратно UI
            }
        }
    }

    override fun onPause() {
        super.onPause()
        shakeDetector?.let {
            sensorManager.unregisterListener(it)
        }
    }
}
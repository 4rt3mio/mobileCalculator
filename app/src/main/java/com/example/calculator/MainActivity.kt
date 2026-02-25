package com.example.calculator

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.calculator.data.model.AppTheme
import com.example.calculator.domain.usecase.CalculatorUseCase
import com.example.calculator.ui.CalculatorScreen
import com.example.calculator.ui.history.HistoryScreen
import com.example.calculator.ui.viewmodel.CalculatorViewModel
import com.example.calculator.ui.viewmodel.CalculatorViewModelFactory
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.utils.ShakeDetector
import com.google.firebase.messaging.FirebaseMessaging
import android.util.Log

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        setContent {
            val viewModel: CalculatorViewModel = viewModel(
                factory = CalculatorViewModelFactory(CalculatorUseCase())
            )
            val theme by viewModel.currentTheme.collectAsStateWithLifecycle()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val history by viewModel.history.collectAsStateWithLifecycle()

            val darkTheme = when (theme) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            var showHistory by remember { mutableStateOf(false) }

            shakeDetector = ShakeDetector {
                viewModel.onShake()
            }

            CalculatorTheme(darkTheme = darkTheme) {
                if (showHistory) {
                    HistoryScreen(
                        historyItems = history,
                        onBack = { showHistory = false }
                    )
                } else {
                    CalculatorScreen(
                        state = state,
                        onAction = viewModel::onAction,
                        onOpenHistory = { showHistory = true },
                        currentTheme = theme,
                        onThemeSelected = viewModel::changeTheme
                    )
                }
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("calculator")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic")
                }
            }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector?.let {
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
                sensorManager.registerListener(it, accelerometer, SensorManager.SENSOR_DELAY_UI)
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
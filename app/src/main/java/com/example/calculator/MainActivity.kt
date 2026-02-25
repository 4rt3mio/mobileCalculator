package com.example.calculator

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.calculator.domain.usecase.CalculatorUseCase
import com.example.calculator.ui.CalculatorScreen
import com.example.calculator.ui.viewmodel.CalculatorViewModel
import com.example.calculator.ui.viewmodel.CalculatorViewModelFactory
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.utils.ShakeDetector

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        setContent {
            CalculatorTheme {
                val viewModel: CalculatorViewModel = viewModel(
                    factory = CalculatorViewModelFactory(CalculatorUseCase())
                )

                shakeDetector = ShakeDetector {
                    viewModel.onShake()
                }

                CalculatorScreen(
                    state = viewModel.state,
                    onAction = viewModel::onAction
                )
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
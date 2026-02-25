package com.example.calculator.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class ShakeDetector(
    private val onShake: () -> Unit
) : SensorEventListener {

    private var lastShakeTime: Long = 0
    private val shakeThreshold = 15f
    private val minInterval = 1000

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            if (acceleration > shakeThreshold) {
                val now = System.currentTimeMillis()
                if (now - lastShakeTime > minInterval) {
                    lastShakeTime = now
                    onShake.invoke()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
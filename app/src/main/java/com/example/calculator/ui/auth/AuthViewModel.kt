package com.example.calculator.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.fragment.app.FragmentActivity
import com.example.calculator.data.security.BiometricManager
import com.example.calculator.data.security.SecurePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val securePrefs = SecurePreferences(application.applicationContext)
    private lateinit var biometricManager: BiometricManager

    private val _isPinSet = MutableStateFlow(false)
    val isPinSet: StateFlow<Boolean> = _isPinSet

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _biometricAvailable = MutableStateFlow(false)
    val biometricAvailable: StateFlow<Boolean> = _biometricAvailable

    init {
        checkPinStatus()
    }

    fun setActivity(activity: FragmentActivity) {
        biometricManager = BiometricManager(activity)
        _biometricAvailable.value = biometricManager.isBiometricAvailable()
    }

    private fun checkPinStatus() {
        viewModelScope.launch {
            _isPinSet.value = securePrefs.isPinSet()
            if (!_isPinSet.value) {
                _isAuthenticated.value = true
            }
        }
    }

    fun setupPin(pin: String, confirmPin: String) {
        if (pin != confirmPin) {
            _errorMessage.value = "Pins do not match"
            return
        }
        if (pin.length < 4) {
            _errorMessage.value = "Pin must be at least 4 digits"
            return
        }
        viewModelScope.launch {
            securePrefs.savePinHash(pin)
            _isPinSet.value = true
            _isAuthenticated.value = true
        }
    }

    fun verifyPin(pin: String) {
        viewModelScope.launch {
            val correct = securePrefs.isPinCorrect(pin)
            if (correct) {
                _isAuthenticated.value = true
            } else {
                _errorMessage.value = "Incorrect PIN"
            }
        }
    }

    fun authenticateWithBiometrics() {
        biometricManager.authenticate(
            onSuccess = {
                _isAuthenticated.value = true
            },
            onError = { error ->
                _errorMessage.value = error
            }
        )
    }

    fun resetPin() {
        viewModelScope.launch {
            securePrefs.clearPin()
            _isPinSet.value = false
            _isAuthenticated.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
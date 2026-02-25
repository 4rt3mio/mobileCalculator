package com.example.calculator.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class SecurePreferences(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val editor = sharedPreferences.edit()

    suspend fun savePinHash(pin: String) {
        val hash = hashPin(pin)
        withContext(Dispatchers.IO) {
            editor.putString("pin_hash", hash).apply()
        }
    }

    suspend fun isPinCorrect(pin: String): Boolean {
        val hash = hashPin(pin)
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString("pin_hash", null) == hash
        }
    }

    suspend fun isPinSet(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.contains("pin_hash")
        }
    }

    suspend fun clearPin() {
        withContext(Dispatchers.IO) {
            editor.remove("pin_hash").apply()
        }
    }

    private fun hashPin(pin: String): String {
        val bytes = pin.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}
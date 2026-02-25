package com.example.calculator.data.firebase

import com.example.calculator.data.model.AppTheme
import com.example.calculator.data.model.UserSettings
import com.example.calculator.domain.repository.IThemeRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ThemeRepository : IThemeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection("settings").document("user")

    override suspend fun saveTheme(theme: AppTheme) {
        val settings = UserSettings(theme = theme)
        docRef.set(settings).await()
    }

    override fun getTheme(): Flow<AppTheme?> = callbackFlow {
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val theme = snapshot?.toObject(UserSettings::class.java)?.theme
            trySend(theme).isSuccess
        }
        awaitClose { listener.remove() }
    }
}
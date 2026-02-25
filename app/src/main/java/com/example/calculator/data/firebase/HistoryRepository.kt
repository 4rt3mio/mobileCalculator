package com.example.calculator.data.firebase

import com.example.calculator.data.model.HistoryItem
import com.example.calculator.domain.repository.IHistoryRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HistoryRepository : IHistoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("history")

    override suspend fun addHistoryItem(expression: String, result: String) {
        val item = HistoryItem(
            expression = expression,
            result = result,
            timestamp = Timestamp.now()
        )
        collection.add(item).await()
    }

    override fun getHistoryItems(limit: Long): Flow<List<HistoryItem>> = callbackFlow {
        val listener = collection.orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(HistoryItem::class.java) ?: emptyList()
                trySend(items).isSuccess
            }
        awaitClose { listener.remove() }
    }

    override suspend fun clearHistory() {
        try {
            val documents = collection.get().await()
            for (doc in documents) {
                doc.reference.delete().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
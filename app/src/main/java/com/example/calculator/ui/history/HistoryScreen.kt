package com.example.calculator.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.data.model.HistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyItems: List<HistoryItem>,
    onBack: () -> Unit,
    onItemClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    onLoadAll: () -> Unit,
    onLoadRecent: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                },
                actions = {
                    IconButton(onClick = onClearHistory) {
                        Text("🗑️")
                    }
                    IconButton(onClick = { onLoadRecent(10) }) {
                        Text("🔄10")
                    }
                    IconButton(onClick = onLoadAll) {
                        Text("📥")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(historyItems) { item ->
                HistoryItemRow(
                    item = item,
                    onClick = { onItemClick(item.expression) }
                )
            }
        }
    }
}

@Composable
fun HistoryItemRow(
    item: HistoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = item.expression, style = MaterialTheme.typography.bodyLarge)
            Text(text = "= ${item.result}", style = MaterialTheme.typography.titleMedium)
            Text(
                text = item.timestamp.toDate().toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
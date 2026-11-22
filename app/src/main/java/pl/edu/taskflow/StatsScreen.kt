package pl.edu.taskflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun StatsScreen(state: TasksUiState) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).testTag("screen_stats"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Statystyki", style = MaterialTheme.typography.headlineMedium)
        Text("Wszystkie zadania: ${state.totalCount}", Modifier.testTag("stats_total"))
        Text("Ukończone: ${state.completedCount}", Modifier.testTag("stats_completed"))
        Text("Postęp: ${state.progressPercent}%", Modifier.testTag("stats_progress"))
        LinearProgressIndicator(progress = { state.progressPercent / 100f })
    }
}

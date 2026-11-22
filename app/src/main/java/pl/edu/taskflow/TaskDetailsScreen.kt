package pl.edu.taskflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun TaskDetailsScreen(
    task: Task?,
    onBack: () -> Unit,
    onToggleTask: () -> Unit,
    onDeleteTask: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).testTag("screen_task_details"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(onClick = onBack, modifier = Modifier.testTag("btn_back")) {
            Text("Wróć")
        }
        if (task == null) {
            Text("Nie znaleziono zadania.", modifier = Modifier.testTag("task_not_found"))
        } else {
            Text(
                task.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.testTag("details_task_title"),
            )
            Text(task.description)
            Text(if (task.isCompleted) "Ukończone" else "Do zrobienia")
            Button(onClick = onToggleTask, modifier = Modifier.testTag("btn_toggle_task")) {
                Text(if (task.isCompleted) "Oznacz jako nieukończone" else "Oznacz jako ukończone")
            }
            Button(onClick = onDeleteTask, modifier = Modifier.testTag("btn_delete_task")) {
                Text("Usuń zadanie")
            }
        }
    }
}

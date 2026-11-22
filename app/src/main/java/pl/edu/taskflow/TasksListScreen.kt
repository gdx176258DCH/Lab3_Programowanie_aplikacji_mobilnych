package pl.edu.taskflow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun TasksListScreen(
    tasks: List<Task>,
    onAddTask: () -> Unit,
    onTaskClick: (String) -> Unit,
    onToggleTask: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                modifier = Modifier.testTag("fab_add_task"),
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .testTag("screen_tasks"),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Zadania", style = MaterialTheme.typography.headlineMedium)
            if (tasks.isEmpty()) {
                Text("Brak zadań. Dodaj pierwsze przyciskiem +.")
            }
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).testTag("tasks_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(tasks, key = { it.id }) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .testTag("task_${task.id}")
                            .clickable { onTaskClick(task.id) },
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { onToggleTask(task.id) },
                                modifier = Modifier.testTag("checkbox_${task.id}"),
                            )
                            Column(Modifier.weight(1f)) {
                                Text(task.title, style = MaterialTheme.typography.titleMedium)
                                Text(if (task.isCompleted) "Ukończone" else "Do zrobienia")
                            }
                        }
                    }
                }
            }
        }
    }
}

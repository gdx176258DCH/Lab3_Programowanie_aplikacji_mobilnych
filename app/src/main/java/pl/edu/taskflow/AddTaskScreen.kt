package pl.edu.taskflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskScreen(
    onSaveTask: (String, String) -> Unit,
    onBack: () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(16.dp).testTag("screen_add_task"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(onClick = onBack, modifier = Modifier.testTag("btn_back")) {
            Text("Wróć")
        }
        Text("Nowe zadanie", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_task_title"),
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth().testTag("input_task_description"),
        )
        Button(
            onClick = { onSaveTask(title, description) },
            enabled = title.isNotBlank(),
            modifier = Modifier.testTag("btn_save_task"),
        ) {
            Text("Zapisz zadanie")
        }
    }
}

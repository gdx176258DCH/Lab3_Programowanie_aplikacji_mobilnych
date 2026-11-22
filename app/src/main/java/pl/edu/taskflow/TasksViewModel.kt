package pl.edu.taskflow

import androidx.lifecycle.ViewModel
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TasksViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState = _uiState.asStateFlow()

    fun addTask(title: String, description: String = ""): Boolean {
        if (title.isBlank()) return false
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            description = description.trim(),
        )
        _uiState.update { state -> state.copy(tasks = state.tasks + task) }
        return true
    }

    fun getTaskById(taskId: String): Task? =
        _uiState.value.tasks.find { it.id == taskId }

    fun toggleTaskCompleted(taskId: String) {
        _uiState.update { state ->
            state.copy(tasks = state.tasks.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            })
        }
    }

    fun deleteTask(taskId: String) {
        _uiState.update { state ->
            state.copy(tasks = state.tasks.filterNot { it.id == taskId })
        }
    }
}

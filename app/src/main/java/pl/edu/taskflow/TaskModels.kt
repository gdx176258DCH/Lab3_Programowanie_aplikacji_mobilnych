package pl.edu.taskflow

data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
)

data class TasksUiState(val tasks: List<Task> = emptyList()) {
    val totalCount: Int get() = tasks.size
    val completedCount: Int get() = tasks.count { it.isCompleted }
    val progressPercent: Int
        get() = if (totalCount == 0) 0 else completedCount * 100 / totalCount
}

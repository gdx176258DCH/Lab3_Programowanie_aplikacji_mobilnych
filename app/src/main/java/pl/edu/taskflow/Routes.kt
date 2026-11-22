package pl.edu.taskflow

import kotlinx.serialization.Serializable

@Serializable
data object TasksListRoute

@Serializable
data object AddTaskRoute

@Serializable
data class TaskDetailsRoute(val taskId: String)

@Serializable
data object StatsRoute

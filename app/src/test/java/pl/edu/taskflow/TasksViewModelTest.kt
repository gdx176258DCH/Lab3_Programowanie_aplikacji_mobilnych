package pl.edu.taskflow

import org.junit.Assert.*
import org.junit.Test

class TasksViewModelTest {
    @Test
    fun blankTitlesAreIgnored() {
        val viewModel = TasksViewModel()
        listOf("", "   ", "\t\n").forEach { title ->
            assertFalse(viewModel.addTask(title))
        }
        assertEquals(0, viewModel.uiState.value.totalCount)
    }

    @Test
    fun validTaskUpdatesListAndTotalCount() {
        val viewModel = TasksViewModel()
        assertTrue(viewModel.addTask("  Laboratorium  ", "  Nawigacja Compose  "))
        val task = viewModel.uiState.value.tasks.single()
        assertEquals("Laboratorium", task.title)
        assertEquals("Nawigacja Compose", task.description)
        assertFalse(task.isCompleted)
        assertTrue(task.id.isNotBlank())
        assertEquals(1, viewModel.uiState.value.totalCount)
        assertEquals(task, viewModel.getTaskById(task.id))
    }

    @Test
    fun togglingCompletionUpdatesProgressInBothDirections() {
        val viewModel = TasksViewModel()
        viewModel.addTask("Pierwsze")
        viewModel.addTask("Drugie")
        val taskId = viewModel.uiState.value.tasks.first().id

        viewModel.toggleTaskCompleted(taskId)
        assertTrue(viewModel.getTaskById(taskId)!!.isCompleted)
        assertEquals(1, viewModel.uiState.value.completedCount)
        assertEquals(50, viewModel.uiState.value.progressPercent)

        viewModel.toggleTaskCompleted(taskId)
        assertFalse(viewModel.getTaskById(taskId)!!.isCompleted)
        assertEquals(0, viewModel.uiState.value.progressPercent)
    }

    @Test
    fun deletingTaskRemovesItAndRecalculatesCounts() {
        val viewModel = TasksViewModel()
        viewModel.addTask("Pierwsze")
        viewModel.addTask("Drugie")
        val taskId = viewModel.uiState.value.tasks.first().id
        viewModel.toggleTaskCompleted(taskId)

        viewModel.deleteTask(taskId)
        assertNull(viewModel.getTaskById(taskId))
        assertEquals("Drugie", viewModel.uiState.value.tasks.single().title)
        assertEquals(1, viewModel.uiState.value.totalCount)
        assertEquals(0, viewModel.uiState.value.completedCount)
        assertEquals(0, viewModel.uiState.value.progressPercent)
    }

    @Test
    fun deletingLastTaskLeavesZeroProgress() {
        val viewModel = TasksViewModel()
        viewModel.addTask("Jedyne")
        val taskId = viewModel.uiState.value.tasks.single().id
        viewModel.toggleTaskCompleted(taskId)
        assertEquals(100, viewModel.uiState.value.progressPercent)
        viewModel.deleteTask(taskId)
        assertEquals(0, viewModel.uiState.value.totalCount)
        assertEquals(0, viewModel.uiState.value.progressPercent)
    }

    @Test
    fun unknownIdentifiersDoNotChangeState() {
        val viewModel = TasksViewModel()
        viewModel.addTask("Istniejące")
        val before = viewModel.uiState.value
        viewModel.toggleTaskCompleted("missing")
        viewModel.deleteTask("missing")
        assertEquals(before, viewModel.uiState.value)
        assertNull(viewModel.getTaskById("missing"))
    }

    @Test
    fun tasksWithSameTitleHaveDistinctIdentifiers() {
        val viewModel = TasksViewModel()
        repeat(2) { viewModel.addTask("Powtórzone") }
        assertEquals(2, viewModel.uiState.value.tasks.map { it.id }.toSet().size)
    }
}

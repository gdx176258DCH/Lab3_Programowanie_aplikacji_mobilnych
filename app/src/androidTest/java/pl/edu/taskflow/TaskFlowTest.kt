package pl.edu.taskflow

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskFlowTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Test
    fun isolatedFormCallsSaveLambdaWithEnteredValues() {
        var savedTask: Pair<String, String>? = null
        composeRule.setContent {
            MaterialTheme {
                AddTaskScreen(
                    onSaveTask = { title, description -> savedTask = title to description },
                    onBack = {},
                )
            }
        }
        composeRule.onNodeWithTag("btn_save_task").assertIsNotEnabled()
        composeRule.onNodeWithTag("input_task_title").performTextInput("   ")
        composeRule.onNodeWithTag("btn_save_task").assertIsNotEnabled()
        composeRule.onNodeWithTag("input_task_title").performTextInput("Test formularza")
        composeRule.onNodeWithTag("input_task_description").performTextInput("Opis zadania")
        composeRule.onNodeWithTag("btn_save_task").performClick()
        composeRule.runOnIdle {
            assertEquals("   Test formularza" to "Opis zadania", savedTask)
        }
    }

    @Test
    fun addingTaskOpeningDetailsAndSwitchingTabs() {
        val viewModel = TasksViewModel()
        setUpApp(viewModel)
        composeRule.runOnIdle {
            assertTrue(navController.currentDestination?.hasRoute<TasksListRoute>() == true)
        }
        composeRule.onNodeWithTag("fab_add_task").performClick()
        composeRule.runOnIdle {
            assertTrue(navController.currentDestination?.hasRoute<AddTaskRoute>() == true)
        }
        composeRule.onNodeWithTag("bottom_navigation").assertDoesNotExist()
        composeRule.onNodeWithTag("input_task_title").performTextInput("Zadanie E2E")
        composeRule.onNodeWithTag("input_task_description").performTextInput("Opis E2E")
        composeRule.onNodeWithTag("btn_save_task").performClick()
        composeRule.runOnIdle {
            assertTrue(navController.currentDestination?.hasRoute<TasksListRoute>() == true)
        }
        val taskId = composeRule.runOnIdle { viewModel.uiState.value.tasks.single().id }
        composeRule.onNodeWithTag("task_$taskId").assertIsDisplayed().performClick()
        composeRule.runOnIdle {
            assertTrue(navController.currentDestination?.hasRoute<TaskDetailsRoute>() == true)
            assertEquals(taskId, navController.currentBackStackEntry!!.toRoute<TaskDetailsRoute>().taskId)
        }
        composeRule.onNodeWithTag("bottom_navigation").assertDoesNotExist()
        composeRule.onNodeWithTag("details_task_title").assertTextEquals("Zadanie E2E")
        composeRule.onNodeWithTag("btn_toggle_task").performClick()
        composeRule.onNodeWithTag("btn_back").performClick()
        composeRule.onNodeWithTag("nav_stats").performClick()
        composeRule.runOnIdle {
            assertTrue(navController.currentDestination?.hasRoute<StatsRoute>() == true)
        }
        composeRule.onNodeWithTag("stats_total").assertTextEquals("Wszystkie zadania: 1")
        composeRule.onNodeWithTag("stats_progress").assertTextEquals("Postęp: 100%")
        composeRule.onNodeWithTag("nav_tasks").performClick()
        composeRule.onNodeWithTag("task_$taskId").performClick()
        composeRule.onNodeWithTag("btn_delete_task").performClick()
        composeRule.runOnIdle {
            assertTrue(navController.currentDestination?.hasRoute<TasksListRoute>() == true)
            assertEquals(0, viewModel.uiState.value.totalCount)
        }
        composeRule.onNodeWithTag("task_$taskId").assertDoesNotExist()
    }

    @Test
    fun taskDetailsDeepLinkDecodesIdentifierAndHandlesMissingTask() {
        val viewModel = TasksViewModel()
        viewModel.addTask("Zadanie z linku")
        val taskId = viewModel.uiState.value.tasks.single().id
        setUpApp(viewModel)
        composeRule.runOnIdle {
            navController.navigate(Uri.parse("taskflow://tasks/$taskId"))
        }
        composeRule.runOnIdle {
            assertEquals(taskId, navController.currentBackStackEntry!!.toRoute<TaskDetailsRoute>().taskId)
        }
        composeRule.onNodeWithTag("details_task_title").assertTextEquals("Zadanie z linku")
        composeRule.onNodeWithTag("bottom_navigation").assertDoesNotExist()
        composeRule.runOnIdle {
            navController.navigate(Uri.parse("taskflow://tasks/missing"))
        }
        composeRule.onNodeWithTag("task_not_found").assertIsDisplayed()
    }

    @Test
    fun switchingTabsRestoresListScrollPosition() {
        val viewModel = TasksViewModel()
        repeat(60) { viewModel.addTask("Zadanie $it") }
        val taskId = viewModel.uiState.value.tasks[40].id
        setUpApp(viewModel)
        composeRule.onNodeWithTag("tasks_list").performScrollToIndex(40)
        composeRule.onNodeWithTag("task_$taskId").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_stats").performClick()
        composeRule.onNodeWithTag("nav_stats").performClick()
        composeRule.onNodeWithTag("nav_tasks").performClick()
        composeRule.onNodeWithTag("task_$taskId").assertIsDisplayed()
    }

    private fun setUpApp(viewModel: TasksViewModel) {
        composeRule.setContent {
            val context = LocalContext.current
            navController = androidx.compose.runtime.remember {
                TestNavHostController(context).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }
            }
            MaterialTheme {
                TaskFlowApp(navController = navController, tasksViewModel = viewModel)
            }
        }
    }
}

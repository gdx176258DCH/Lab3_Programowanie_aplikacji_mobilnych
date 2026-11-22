package pl.edu.taskflow

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute

@Composable
fun TaskFlowApp(
    navController: NavHostController = rememberNavController(),
    tasksViewModel: TasksViewModel = viewModel(),
) {
    val state by tasksViewModel.uiState.collectAsStateWithLifecycle()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val showBottomBar = destination?.hasRoute<TasksListRoute>() == true ||
        destination?.hasRoute<StatsRoute>() == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(Modifier.testTag("bottom_navigation")) {
                    NavigationBarItem(
                        selected = destination?.hierarchy?.any {
                            it.hasRoute<TasksListRoute>()
                        } == true,
                        onClick = {
                            navController.navigate(TasksListRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("☑") },
                        label = { Text("Zadania") },
                        modifier = Modifier.testTag("nav_tasks"),
                    )
                    NavigationBarItem(
                        selected = destination?.hierarchy?.any {
                            it.hasRoute<StatsRoute>()
                        } == true,
                        onClick = {
                            navController.navigate(StatsRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("▥") },
                        label = { Text("Statystyki") },
                        modifier = Modifier.testTag("nav_stats"),
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TasksListRoute,
            modifier = Modifier.padding(padding),
        ) {
            composable<TasksListRoute> {
                TasksListScreen(
                    tasks = state.tasks,
                    onAddTask = { navController.navigate(AddTaskRoute) },
                    onTaskClick = { taskId ->
                        navController.navigate(TaskDetailsRoute(taskId))
                    },
                    onToggleTask = tasksViewModel::toggleTaskCompleted,
                )
            }
            composable<AddTaskRoute> {
                AddTaskScreen(
                    onSaveTask = { title, description ->
                        if (tasksViewModel.addTask(title, description)) {
                            navController.navigateUp()
                        }
                    },
                    onBack = { navController.navigateUp() },
                )
            }
            composable<TaskDetailsRoute>(
                deepLinks = listOf(
                    navDeepLink<TaskDetailsRoute>(basePath = "taskflow://tasks"),
                ),
            ) { entry ->
                val args = entry.toRoute<TaskDetailsRoute>()
                TaskDetailsScreen(
                    task = tasksViewModel.getTaskById(args.taskId),
                    onBack = { navController.navigateUp() },
                    onToggleTask = { tasksViewModel.toggleTaskCompleted(args.taskId) },
                    onDeleteTask = {
                        tasksViewModel.deleteTask(args.taskId)
                        navController.navigateUp()
                    },
                )
            }
            composable<StatsRoute> {
                StatsScreen(state)
            }
        }
    }
}

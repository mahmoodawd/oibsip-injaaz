package dev.awd.injaaz.presentation.tasks.tasklist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeRight
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.ui.theme.InjaazTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private var tempTask = Task(
        0,
        "New Task",
        "",
        false,
        0,
        0,
        Priority.HIGH
    )

    @Before
    fun setup() {
        composeTestRule.setContent {
            InjaazTheme {
                TasksScreen(
                    tasksUiState = TasksUiState.Tasks(
                        listOf(tempTask)
                    ),
                    onTaskLongClick = {},
                    onTaskDismissed = {},
                    onTaskChecked = { _, isChecked ->
                        tempTask = tempTask.copy(isCompleted = isChecked)
                    },
                )
            }
        }
    }

    @Test
    fun onLaunch_tasksShown() {
        composeTestRule.onNodeWithText(tempTask.title).assertExists()
    }

    @Test
    fun clickTask_taskIsToggleable() {
        composeTestRule.onNodeWithText(tempTask.title).performClick()
        Assert.assertTrue(tempTask.isCompleted)
        composeTestRule.onNodeWithText(tempTask.title).performClick()
        Assert.assertFalse(tempTask.isCompleted)
    }

    @Test
    fun dismissTask_taskDismissed() {
        composeTestRule.onNodeWithText(tempTask.title).performTouchInput { swipeRight() }
            .assertDoesNotExist()
    }
}
package dev.awd.injaaz.presentation


import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dev.awd.injaaz.R
import dev.awd.injaaz.presentation.tasks.tasklist.TasksScreen
import dev.awd.injaaz.presentation.tasks.tasklist.TasksUiState
import dev.awd.injaaz.ui.theme.InjaazTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity by lazy { composeTestRule.activity }

    @Before
    fun setup() {
        composeTestRule.setContent {
            InjaazTheme {
                HomeScreen(
                    userName = "",
                    userAvatar = "",
                    onAddButtonClick = { },
                    onUserAvatarClick = { },
                    bottomNavigationItems = BottomBarScreen.entries,
                    currentScreen = BottomBarScreen.Tasks,
                    currentScreenBody = {
                        TasksScreen(
                            tasksUiState = TasksUiState.Empty,
                            onTaskLongClick = {},
                            onTaskDismissed = {},
                            onTaskChecked = { _, _ -> }
                        )
                    },
                    onScreenChange = {}
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("Setup")
    }

    @Test
    fun app_launches() {

        findAddNewTaskFab().assertIsDisplayed()

        findWelcomeBackText().assertIsDisplayed()

        findNoTasksView().assertIsDisplayed()
    }

    @Test
    fun addNewTask_firesNewTask() {
        findAddNewTaskFab().performClick()
    }

    private fun findAddNewTaskFab() = composeTestRule.onNodeWithContentDescription(
        activity.getString(R.string.new_task)
    ).onParent()

    private fun findWelcomeBackText() = composeTestRule.onNodeWithText(
        activity.getString(R.string.welcome_back),
        useUnmergedTree = true
    )

    private fun findNoTasksView() = composeTestRule.onNodeWithText(
        activity.getString(R.string.no_tasks_yet).uppercase(), useUnmergedTree = true
    )
}
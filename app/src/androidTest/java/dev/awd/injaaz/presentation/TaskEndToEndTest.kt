package dev.awd.injaaz.presentation

import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.awd.injaaz.MainActivity
import dev.awd.injaaz.R
import dev.awd.injaaz.di.AppModule
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.domain.models.User
import dev.awd.injaaz.navigation.HomeDest
import dev.awd.injaaz.navigation.InjaazNavHost
import dev.awd.injaaz.presentation.auth.GoogleAuthUiClient
import dev.awd.injaaz.ui.theme.InjaazTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class TaskEndToEndTest {

    @get:Rule(order = 0) //To inject dependencies first
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val activity by lazy { composeTestRule.activity }
    private lateinit var navController: TestNavHostController
    private val testUser by lazy {
        User("0", "Awad", "awd@gmail.com", "")
    }
    private val testTask by lazy {
        Task(
            id = 25,
            title = "Test Task",
            details = "Task Details",
            date = 0,
            time = 0,
            priority = Priority.MODERATE
        )
    }


    @Before
    fun setup() {
        hiltRule.inject()
        activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            InjaazTheme {
                Surface(
                    color = Color.Black
                ) {
                    InjaazNavHost(
                        navController = navController,
                        startDestination = HomeDest.route,
                        googleAuthUiClient = GoogleAuthUiClient(
                            LocalContext.current
                        ), currentUser = testUser
                    )
                }
            }
        }
    }


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun addTask_checkUnCheck_EditAfterwards() {

        //Add task
        composeTestRule.waitUntilAtLeastOneExists(
            timeoutMillis = 10000,
            matcher = hasContentDescription(activity.getString(R.string.new_task))
        )
        findAddButton().performClick()
        findTitleTextField().assertIsDisplayed().performTextInput(testTask.title)
        findDetailsTextField().performTextInput(testTask.details)
        closeSoftKeyboard()
        findPriorityWithLabelId(R.string.priority_high).performClick()
        findSaveButton().performClick()

        //Check if Added
        composeTestRule.waitUntilAtLeastOneExists(
            timeoutMillis = 10000,
            matcher = hasText(testTask.title)
        )
        findTaskWithTitle(testTask.title).assertIsDisplayed()

        //Edit

        findTaskWithTitle(testTask.title).performClick() //Mark as completed
        findTaskWithTitle(testTask.title).performClick() //Mark as uncompleted
        findTaskWithTitle(testTask.title).performTouchInput { longClick(durationMillis = 5000) } //Navigate to details screen
        findTitleTextField().assertIsDisplayed()
        findTitleTextField().performTextClearance()
        findTitleTextField().performTextInput("Edited Task")
        closeSoftKeyboard()
        findPriorityWithLabelId(R.string.priority_low).performClick()
        findSaveButton().performClick()

        //Check if Edited
        findTaskWithTitle("Edited Task").assertIsDisplayed()
    }


    private fun findAddButton() = composeTestRule.onNodeWithContentDescription(
        activity.getString(R.string.new_task)
    )

    private fun findTitleTextField() = composeTestRule.onNode(
        hasSetTextAction() and hasContentDescription(
            activity.getString(R.string.task_title_hint)
        )
    )

    private fun findDetailsTextField() = composeTestRule.onNode(
        hasSetTextAction() and hasContentDescription(
            activity.getString(R.string.describe_your_task)
        )
    )

    private fun findPriorityWithLabelId(labelId: Int) = composeTestRule.onNode(
        hasStateDescription(activity.getString(labelId)) and hasClickAction()
    )

    private fun findSaveButton() =
        composeTestRule.onNode(
            hasText(activity.getString(R.string.save)) and hasClickAction()
        )

    private fun findTaskWithTitle(title: String) = composeTestRule.onNodeWithText(title)


}


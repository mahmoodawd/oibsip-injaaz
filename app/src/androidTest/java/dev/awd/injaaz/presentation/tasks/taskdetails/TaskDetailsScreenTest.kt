package dev.awd.injaaz.presentation.tasks.taskdetails

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.utils.formatDate
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class NewTaskTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity by lazy { composeTestRule.activity }
    private var isCreateButtonEnabled = false
    private var taskCreated: Boolean = false
    private var taskDate: Long = 0L
    private var taskTime: Int = 0
    private var showDatePickerDialog = false
    private var showTimePickerDialog = false


    @OptIn(ExperimentalMaterial3Api::class)
    @Before
    fun setup() {
        composeTestRule.setContent {
            var uiState by remember {
                mutableStateOf(
                    TaskDetailsUiState.TaskDetails()
                )
            }

            val calendar = Calendar.getInstance()
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = uiState.taskDate
            )
            val timePickerState =
                rememberTimePickerState(initialHour = calendar.get(Calendar.HOUR_OF_DAY))


            Surface(color = Color.Black) {
                InjaazTheme {
                    TasksDetailsScreen(
                        uiState = uiState,
                        taskPriorities = Priority.entries,
                        isCreateButtonEnabled = isCreateButtonEnabled,
                        onTitleChanged = {
                            uiState = uiState.copy(taskTitle = it)
                            isCreateButtonEnabled = it.isNotBlank()
                        },
                        onTaskDetailsChanged = {
                            uiState = uiState.copy(taskDetails = it)
                        },
                        onPriorityChanged = {
                            uiState = uiState.copy(taskPriority = it)
                        },
                        showDatePickerDialog = showDatePickerDialog,
                        showTimePickerDialog = showTimePickerDialog,
                        onDatePickerClicked = { showDatePickerDialog = true },
                        onTimePickerClicked = { showTimePickerDialog = true },
                        datePickerState = datePickerState,
                        timePickerState = timePickerState,
                        onDateChanged = {
                            taskDate = datePickerState.selectedDateMillis!!
                            showDatePickerDialog = false
                        },
                        onDatePickerDismissed = { },
                        onTimePickerDismissed = { },
                        onTimeChanged = { taskTime = timePickerState.hour },
                        onCreateTask = {
                            taskCreated = true
                        }, onBackPressed = {})
                }
            }
        }
    }

    @Test
    fun saveButton_enableToggles() {

        findSaveButton().assertIsNotEnabled()
        findTaskTitleInputField().performTextInput("New Task")
        findTaskTitleInputField().assertTextContains("New Task")
        findSaveButton().assertIsEnabled()
    }

    @Test
    fun editDetails_detailsUpdated() {
        findTaskDetailsInputField().performTextInput("Task Details")
        findTaskDetailsInputField().assertTextContains("Task Details")
    }

    @Test
    fun onLaunch_priorityIsModerate() {
        findTaskPriorityRadioButtonLabeled(activity.getString(R.string.priority_moderate))
            .assertIsSelected()
    }


    @Test
    fun selectPriority_priorityChanged() {
        findTaskPriorityRadioButtonLabeled(activity.getString(R.string.priority_high))
            .assertIsNotSelected()
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun taskDateButton_opensDialog() {
        findTaskDateButton().performClick()
        Assert.assertTrue(showDatePickerDialog)
    }


    @Test
    fun saveTask_taskIsSaved() {
        findTaskTitleInputField().performTextInput("New Task")
        findSaveButton().assertIsEnabled().performClick()
        Assert.assertTrue(taskCreated)

    }

    private fun findSaveButton() =
        composeTestRule.onNode(
            hasText(activity.getString(R.string.save)) and hasClickAction()
        )

    private fun findTaskTitleInputField() = composeTestRule.onNode(
        hasSetTextAction() and hasContentDescription(
            activity.getString(R.string.task_title_hint)
        )
    )

    private fun findTaskDetailsInputField() = composeTestRule.onNode(
        hasSetTextAction() and hasContentDescription(
            activity.getString(R.string.describe_your_task)
        )
    )

    private fun findTaskPriorityRadioButtonLabeled(label: String) = composeTestRule.onNode(
        hasStateDescription(label) and hasClickAction()
    )

    private fun findTaskDateButton() = composeTestRule.onNode(
        hasClickAction() and hasContentDescription(formatDate(taskDate))
    )
}
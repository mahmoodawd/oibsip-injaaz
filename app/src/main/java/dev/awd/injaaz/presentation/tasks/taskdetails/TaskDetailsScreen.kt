@file:OptIn(ExperimentalMaterial3Api::class)

package dev.awd.injaaz.presentation.tasks.taskdetails

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.utils.extractHourFormattedFromDate
import dev.awd.injaaz.utils.formatDate
import dev.awd.injaaz.utils.getTimeInMillis
import timber.log.Timber
import java.util.Calendar
import java.util.Locale

/**
 * Stateful screen for adding or editing tasks
 */
@Composable
fun TaskDetailsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: TaskDetailsViewModel = hiltViewModel(),
) {

    val uiState by viewModel.taskDetailsUiState.collectAsStateWithLifecycle()
    val isCreateButtonEnabled by viewModel.isCreateButtonEnabled.collectAsStateWithLifecycle()
    val taskHasBeenSaved by viewModel.taskHasBeenSaved.collectAsStateWithLifecycle()
    val allPriorities = Priority.entries

    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis
    )
    val timePickerState =
        rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY).plus(1),
            initialMinute = 0
        )

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = taskHasBeenSaved) {
        if (taskHasBeenSaved) onBackPressed()
    }

    TasksDetailsScreen(
        uiState = uiState,
        taskPriorities = allPriorities,
        isCreateButtonEnabled = isCreateButtonEnabled,
        onTitleChanged = viewModel::onTaskTitleChanged,
        onTaskDetailsChanged = viewModel::onTaskDetailsChanged,
        onDateChanged = {
            viewModel.onTaskDateChanged(datePickerState.selectedDateMillis!!)
            showDatePickerDialog = false
        },
        onTimeChanged = {
            viewModel.onTaskTimeChanged(
                getTimeInMillis(hour = timePickerState.hour, minute = timePickerState.minute)
            )
            Timber.i(
                extractHourFormattedFromDate(
                    getTimeInMillis(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                )
            )

            showTimePickerDialog = false
        },
        onPriorityChanged = viewModel::onTaskPriorityChanged,
        showDatePickerDialog = showDatePickerDialog,
        showTimePickerDialog = showTimePickerDialog,
        onDatePickerClicked = { showDatePickerDialog = true },
        onTimePickerClicked = { showTimePickerDialog = true },
        onDatePickerDismissed = {
            showDatePickerDialog = false
            datePickerState.selectedDateMillis = calendar.timeInMillis
        },
        onTimePickerDismissed = { showTimePickerDialog = false },
        datePickerState = datePickerState,
        timePickerState = timePickerState,
        onCreateTask = viewModel::saveTask,
        onBackPressed = onBackPressed,
        modifier = modifier
    )

}

/**
 * Stateless Screen for adding to editing tasks
 */
@Composable
fun TasksDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: TaskDetailsUiState,
    taskPriorities: List<Priority>,
    isCreateButtonEnabled: Boolean,
    onTitleChanged: (String) -> Unit,
    onTaskDetailsChanged: (String) -> Unit,
    onDateChanged: () -> Unit,
    onTimeChanged: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    showDatePickerDialog: Boolean,
    showTimePickerDialog: Boolean,
    datePickerState: DatePickerState,
    timePickerState: TimePickerState,
    onTimePickerClicked: () -> Unit,
    onDatePickerClicked: () -> Unit,
    onDatePickerDismissed: () -> Unit,
    onTimePickerDismissed: () -> Unit,
    onCreateTask: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .scrollable(rememberScrollState(), Orientation.Vertical)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(
            screenTitle = stringResource(
                R.string.task_details
            ),
            onBackPressed = onBackPressed,
            actions = {
                IconButton(
                    onClick = onCreateTask,
                    enabled = isCreateButtonEnabled,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ticksquare),
                        contentDescription = stringResource(id = R.string.save),
                    )
                }
            })
        Spacer(modifier = Modifier.height(4.dp))
        when (uiState) {
            is TaskDetailsUiState.Error -> {}
            TaskDetailsUiState.Loading -> CircularProgressIndicator()

            is TaskDetailsUiState.TaskDetails -> {

                TaskPropertyEntry(label = stringResource(R.string.task_title)) {
                    CustomTextField(
                        value = uiState.taskTitle,
                        singleLine = true,
                        hint = stringResource(R.string.task_title_hint),
                        onValueChanged = onTitleChanged
                    )
                }

                TaskPropertyEntry(label = stringResource(id = R.string.task_details)) {
                    CustomTextField(
                        value = uiState.taskDetails,
                        hint = stringResource(R.string.describe_your_task),
                        maxLines = 5,
                        onValueChanged = onTaskDetailsChanged
                    )
                }

                TaskPropertyEntry(label = stringResource(R.string.set_priority)) {
                    PrioritySection(
                        taskPriorities = taskPriorities,
                        selectedPriority = uiState.taskPriority,
                        onPriorityChanged = onPriorityChanged
                    )
                }

                TaskPropertyEntry(label = stringResource(R.string.date_time)) {
                    DateTimeSection(
                        taskTime = extractHourFormattedFromDate(uiState.taskTime),
                        taskDate = formatDate(uiState.taskDate),
                        showDatePickerDialog = showDatePickerDialog,
                        showTimePickerDialog = showTimePickerDialog,
                        datePickerState = datePickerState,
                        timePickerState = timePickerState,
                        onTimePickerClicked = onTimePickerClicked,
                        onDatePickerClicked = onDatePickerClicked,
                        onTimeChanged = onTimeChanged,
                        onDateChanged = onDateChanged,
                        onDatePickerDismissed = onDatePickerDismissed,
                        onTimePickerDismissed = onTimePickerDismissed
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    enabled = isCreateButtonEnabled,
                    onClick = onCreateTask,
                    shape = RoundedCornerShape(size = 6.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskPropertyEntry(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable (() -> Unit),
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        content()
    }
}

/**
 * Custom TextField with label at the topStart.
 * @param modifier optional Modifier to be applied to this text field.
 * @param value the input text to be shown in the text field.
 * @param hint optional text to be shown inside the text field.
 * @param singleLine determines whether the textField is a singleLine.
 * @param maxLines the maximum number of visible lines. This parameter is ignored when singleLine is true.
 * @param onValueChanged the callback triggered when the input service updates the text.
 *
 */
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    onValueChanged: (String) -> Unit,
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.primary,
        unfocusedTextColor = MaterialTheme.colorScheme.primary,
        focusedPlaceholderColor = Color(0xFF6F8793),
        unfocusedPlaceholderColor = Color(0xFF6F8793),
        focusedContainerColor = MaterialTheme.colorScheme.onBackground,
        unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )
    TextField(
        value = value,
        placeholder = { Text(text = hint) },
        onValueChange = onValueChanged,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(imeAction = if (singleLine) ImeAction.Next else ImeAction.Default ),
        colors = textFieldColors,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = TaskPropertyEntryDefaultPadding)
            .semantics { contentDescription = hint }
    )
}


@Composable
fun PrioritySection(
    modifier: Modifier = Modifier,
    taskPriorities: List<Priority>,
    selectedPriority: Priority,
    onPriorityChanged: (Priority) -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        taskPriorities.forEach { priority ->
            RadioButtonWithLabel(
                label = stringResource(id = priority.title),
                selected = priority == selectedPriority
            ) {
                onPriorityChanged(priority)
            }
        }
    }
}


/**
 * Custom Radio Button with horizontal label
 */
@Composable
fun RadioButtonWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.secondary,
    selected: Boolean = false,
    onSelected: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics {
                stateDescription = label
            }
            .selectable(
                role = Role.RadioButton, selected = selected,
                onClick = onSelected
            )
            .padding(TaskPropertyEntryDefaultPadding),
    ) {
        Text(
            text = label,
            color = labelColor,
        )
        RadioButton(
            selected = selected,
            onClick = null,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(
    modifier: Modifier = Modifier,
    taskTime: String,
    taskDate: String,
    showDatePickerDialog: Boolean,
    showTimePickerDialog: Boolean,
    datePickerState: DatePickerState,
    timePickerState: TimePickerState,
    onTimePickerClicked: () -> Unit,
    onDatePickerClicked: () -> Unit,
    onTimeChanged: () -> Unit,
    onDateChanged: () -> Unit,
    onDatePickerDismissed: () -> Unit,
    onTimePickerDismissed: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .padding(TaskPropertyEntryDefaultPadding)
    ) {
        PickerItem(
            text = taskDate,
            icon = R.drawable.calendar,
            onClick = onDatePickerClicked,
            showDialog = showDatePickerDialog,
            onPicked = onDateChanged,
            onDismissed = onDatePickerDismissed
        ) {
            DatePicker(
                headline = null,
                title = null,
                state = datePickerState, showModeToggle = false, colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    yearContentColor = MaterialTheme.colorScheme.onSurface,
                    //YearPicker menu button takes the onSurfaceVariant color(Not included here)
                )
            )
        }
        PickerItem(
            text = taskTime,
            icon = R.drawable.clock,
            showDialog = showTimePickerDialog,
            onClick = onTimePickerClicked,
            onPicked = onTimeChanged,
            onDismissed = onTimePickerDismissed
        ) {
            TimePicker(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp),
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                    periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.onSurface,
                    clockDialColor = MaterialTheme.colorScheme.onBackground,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.onBackground,
                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerItem(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    showDialog: Boolean = false,
    onPicked: () -> Unit,
    onDismissed: () -> Unit,
    dialogContent: @Composable (ColumnScope.() -> Unit),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .semantics { this.contentDescription = text }
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(42.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(4.dp)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }

    if (showDialog) {
        DatePickerDialog(
            shape = RectangleShape,
            tonalElevation = 12.dp,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = onPicked) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissed) {
                    Text(text = stringResource(R.string.cancel))
                }
            },

            content = dialogContent
        )
    }

}

@Preview
@Composable
private fun TaskDetailsPreview() {
    InjaazTheme {
        TasksDetailsScreen(
            uiState = TaskDetailsUiState.TaskDetails(),
            taskPriorities = Priority.entries,
            isCreateButtonEnabled = false,
            onTitleChanged = {},
            onTaskDetailsChanged = {},
            onDateChanged = {},
            onTimeChanged = {},
            onPriorityChanged = {},
            showTimePickerDialog = false,
            showDatePickerDialog = false,
            onTimePickerClicked = {},
            onDatePickerClicked = {},
            onDatePickerDismissed = { },
            onTimePickerDismissed = { },
            datePickerState = DatePickerState(
                locale = Locale.getDefault(),
                initialSelectedDateMillis = 0,
                yearRange = 2023..2025,
                initialDisplayMode = DisplayMode.Picker
            ),
            timePickerState = TimePickerState(0, 0, false),
            onCreateTask = {},
            onBackPressed = {})
    }
}

private val TaskPropertyEntryDefaultPadding = 8.dp
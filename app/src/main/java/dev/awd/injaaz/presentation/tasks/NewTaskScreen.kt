package dev.awd.injaaz.presentation.tasks

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.utils.extractDateFormatted
import dev.awd.injaaz.utils.extractHourFormatted
import java.util.Calendar

@Composable
fun NewTaskScreen(
    modifier: Modifier = Modifier,
    onAddTask: (Task) -> Unit = {},
    onBackPressed: () -> Unit
) {
    var taskTitle = ""
    var taskDetails = ""
    var taskDate = ""
    var taskTime = ""
    var taskPriority: Priority = Priority.MODERATE

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(screenTitle = "Create New Task") { onBackPressed() }
        CustomEditText(label = "Task Title", hint = "Ex. Travel", onValueChanged = {
            taskTitle = it
        })
        CustomEditText(
            label = "Task Details",
            hint = "Ex.In this day I will travel to a certain place for work",
            singleLine = false,
            onValueChanged = { taskDetails = it })
        PrioritySection(onPriorityChanged = { taskPriority = it })
        TimeDateSection(onDateChanged = { taskDate = it }, onTimeChanged = { taskTime = it })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onAddTask(
                    Task(
                        title = taskTitle,
                        description = taskDetails,
                        date = taskDate,
                        priority = taskPriority
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Text(
                text = "Create".uppercase(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    label: String,
    hint: String = label,
    singleLine: Boolean = true,
    onValueChanged: (String) -> Unit
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }
    Column(modifier = modifier) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            value = text,
            placeholder = { Text(text = hint) },
            onValueChange = {
                text = it
                onValueChanged(it)
            },
            singleLine = singleLine,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                focusedPlaceholderColor = Color(0xFF6F8793),
                unfocusedPlaceholderColor = Color(0xFF6F8793),
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun PrioritySection(
    modifier: Modifier = Modifier,
    onPriorityChanged: (Priority) -> Unit,
) {
    val priorityList = listOf(
        Priority.LOW, Priority.MODERATE, Priority.HIGH
    )
    var selectedPriorityIndex by rememberSaveable {
        mutableIntStateOf(1)
    }
    Column(modifier = modifier) {
        Text(
            text = "Set Priority",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            priorityList.forEachIndexed { index, priority ->
                Text(
                    text = priority.name,
                    color = MaterialTheme.colorScheme.secondary
                )
                RadioButton(selected = selectedPriorityIndex == index, onClick = {
                    selectedPriorityIndex = index
                    onPriorityChanged(priority)
                })
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDateSection(
    modifier: Modifier = Modifier,
    onTimeChanged: (String) -> Unit,
    onDateChanged: (String) -> Unit,
) {
    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    val timePickerState = rememberTimePickerState(initialHour = calendar.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
    calendar.set(Calendar.MINUTE, timePickerState.minute)
    calendar.isLenient = false

    Column(modifier = modifier) {
        Text(
            text = "Time & Date",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        PickerItem(
            text = extractHourFormatted(calendar.timeInMillis),
            icon = R.drawable.clock,
            onPicked = {
                onTimeChanged(timePickerState.hour.toString())
            }) {
            TimePicker(state = timePickerState)
        }
        PickerItem(
            text = extractDateFormatted(datePickerState.selectedDateMillis!!),
            icon = R.drawable.calendar,
            onPicked = { onDateChanged(datePickerState.selectedDateMillis.toString()) }
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }
}

@Composable
fun PickerItem(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    onPicked: () -> Unit,
    content: @Composable () -> Unit,
) {
    var showPickerDialog by remember {
        mutableStateOf(false)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable { showPickerDialog = true }
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

    if (showPickerDialog) {
        AlertDialog(
            title = { Text(text = text) },
            onDismissRequest = { showPickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showPickerDialog = false
                    onPicked()
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPickerDialog = false
                }) {
                    Text(text = "Cancel")
                }
            },
            text = content
        )
    }
}

@Preview
@Composable
private fun NewTaskPreview() {
    InjaazTheme {
        NewTaskScreen(onAddTask = {}) {

        }
    }
}



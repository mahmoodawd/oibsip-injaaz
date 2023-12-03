package dev.awd.injaaz.presentation.tasks

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.ui.theme.pilat_extended
import dev.awd.injaaz.utils.capitalize
import dev.awd.injaaz.utils.extractDateFormatted


@Composable
fun TaskDetailsRoute(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    taskId: Int,
    viewModel: TaskDetailsViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = taskId) {

        viewModel.getTaskDetails(taskId)
    }
    val uiState by viewModel.taskDetailsUiStateState.collectAsState()
    TasksDetailsScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        modifier = modifier
    )

}

@Composable
fun TasksDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: TaskDetailsUiState,
    onBackPressed: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(screenTitle = "Task Details", onBackPressed = onBackPressed, actions = {
            Icon(
                painter = painterResource(id = R.drawable.edit), contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.minimumInteractiveComponentSize()
            )
        })
        when (uiState) {
            TaskDetailsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is TaskDetailsUiState.Error -> {
                Text(text = uiState.msg)
            }

            is TaskDetailsUiState.TaskDetails -> {
                val taskDetails = uiState.task

                Text(
                    text = taskDetails.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = pilat_extended,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    PropertyItem(
                        icon = R.drawable.calendar,
                        title = "Due Date",
                        subtitle = extractDateFormatted(taskDetails.date)
                    )
                    PropertyItem(
                        icon = R.drawable.priority,
                        title = "Priority",
                        subtitle = taskDetails.priority.name.lowercase().capitalize()
                    )
                }
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = taskDetails.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
               /* Switch(
                    checked = true,
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
                        uncheckedIconColor = MaterialTheme.colorScheme.onBackground,
                        checkedIconColor = MaterialTheme.colorScheme.primary
                    ),
                    thumbContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.tickcircle),
                            contentDescription = null,
                        )
                    },
                    onCheckedChange = {},
                    modifier = Modifier
                        .align(CenterHorizontally)
                )*/

            }
        }
    }

}

@Composable
fun PropertyItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    subtitle: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(end = 8.dp)
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
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}


@Preview
@Composable
private fun TaskDetailsPreview() {
    InjaazTheme {
        TasksDetailsScreen(
            uiState = TaskDetailsUiState.TaskDetails(
                task = Task(
                    title = "Task Title",
                    description = "Task Description",
                    date = 125574,
                    priority = Priority.MODERATE,
                )
            ),
            onBackPressed = {},
        )
    }
}
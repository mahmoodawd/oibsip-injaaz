package dev.awd.injaaz.presentation.tasks.tasklist

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.presentation.components.InjaazSearchBar
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.utils.extractDateFormatted
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TasksRoute(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel(),
    onTaskClick: (Int) -> Unit
) {

    val tasksUiState by viewModel.tasksUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.loadTasks()
    }
    LaunchedEffect(key1 = true) {
        viewModel.tasksEffect.collectLatest {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    TasksScreen(
        tasksUiState = tasksUiState,
        onTaskClick = onTaskClick,
        onTaskDismissed = viewModel::deleteTask,
        onTaskChecked = viewModel::updateTask,
        modifier = modifier
    )
}

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    tasksUiState: TasksUiState,
    onTaskClick: (Int) -> Unit,
    onTaskDismissed: (Task) -> Unit,
    onTaskChecked: (Task, Boolean) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (tasksUiState) {
            TasksUiState.Empty -> NoTasksView()

            TasksUiState.Loading -> CircularProgressIndicator(
                Modifier
                    .align(CenterHorizontally)
                    .scale(1.5f)
            )

            is TasksUiState.Tasks -> {
                InjaazSearchBar(hint = stringResource(R.string.search_in_tasks), onValueChanged = {}, onFilter = {})
                TasksView(
                    tasks = tasksUiState.tasks,
                    onTaskClick = onTaskClick,
                    onTaskDismissed = onTaskDismissed,
                    onTaskChecked = onTaskChecked
                )
            }

        }
    }
}


@Composable
fun NoTasksView(
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.welcome_image),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.no_tasks_yet).uppercase(),
            color = Color.White,
            letterSpacing = 4.sp,
            fontSize = 32.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksView(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onTaskClick: (Int) -> Unit,
    onTaskDismissed: (Task) -> Unit,
    onTaskChecked: (Task, Boolean) -> Unit,
) {
    LazyColumn(state = rememberLazyListState(), modifier = modifier) {
        stickyHeader {
            Text(
                text = stringResource(R.string.all_tasks),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        items(tasks, key = { it.id }) { task ->
            var isTaskCompleted by remember {
                mutableStateOf(task.isCompleted)

            }
            SwipableTaskItem(
                title = task.title,
                isCompleted = isTaskCompleted,
                onTaskClick = { onTaskClick(task.id) },
                onTaskDismissed = { onTaskDismissed(task) },
                onTaskChecked = { isChecked ->
                    isTaskCompleted = isChecked
                    onTaskChecked(task, isChecked)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipableTaskItem(
    modifier: Modifier = Modifier,
    title: String,
    isCompleted: Boolean,
    onTaskClick: () -> Unit,
    onTaskDismissed: () -> Unit,
    onTaskChecked: (Boolean) -> Unit
) {
    var dismiss by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToEnd) {
                dismiss = true
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )

    LaunchedEffect(key1 = dismiss) {
        if (dismiss) {
            delay(800)
            onTaskDismissed()
        }
    }

    AnimatedVisibility(visible = !dismiss, exit = fadeOut(spring())) {

        SwipeToDismiss(modifier = modifier,
            state = dismissState,
            directions = setOf(DismissDirection.StartToEnd),
            background = { DismissBackground() },
            dismissContent = {
                TaskItem(
                    title = title,
                    isCompleted = isCompleted,
                    onTaskClick = onTaskClick,
                    onTaskChecked = onTaskChecked
                )
            })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissDirection: DismissDirection = DismissDirection.StartToEnd) {
    val color = when (dismissDirection) {
        DismissDirection.StartToEnd -> Color(0xFFFF1744)
        DismissDirection.EndToStart -> Color(0xFF1DE9B6)
        null -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxSize()
            .background(color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (dismissDirection == DismissDirection.StartToEnd) Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        if (dismissDirection == DismissDirection.EndToStart) Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Archive"
        )
    }
}


@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    title: String,
    isCompleted: Boolean,
    onTaskClick: () -> Unit,
    onTaskChecked: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 6.dp)
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
            color = Color.White,
            modifier = Modifier
                .clickable { onTaskClick() }
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier
            .weight(1f)
            .clickable { onTaskClick() })
        Checkbox(
            checked = isCompleted, colors = CheckboxDefaults.colors(
                checkmarkColor = Color.Black,
                uncheckedColor = Color.Black
            ), onCheckedChange = onTaskChecked,
            modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun CompletedTasksRow(
    modifier: Modifier = Modifier,
    tasks: List<Task>
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.completed_tasks),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
        )
        LazyRow(modifier = Modifier.align(CenterHorizontally)) {
            items(tasks) { task ->
                TasksRowItem(title = task.title, date = extractDateFormatted(task.date))
            }
        }
    }

}

@Composable
fun TasksRowItem(
    modifier: Modifier = Modifier,
    title: String,
    date: String
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.primary, RectangleShape)
            .width(132.dp)
            .height(70.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            softWrap = true,
            color = Color.Black,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = date,
            modifier = Modifier.padding(4.dp)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OngoingTasksColumn(
    modifier: Modifier = Modifier,
    tasks: List<Task>
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            Text(
                text = stringResource(R.string.ongoing_tasks),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        items(tasks) {
            OngoingTaskItem(title = it.title, date = it.date)
        }
    }
}

@Composable
fun OngoingTaskItem(
    modifier: Modifier = Modifier,
    title: String,
    date: Long
) {
}


@Preview
@Composable
private fun TasksPreview() {
    InjaazTheme {
        TasksScreen(
            tasksUiState = TasksUiState.Tasks(
                tasks = listOf(
                    Task(
                        id = 1,
                        title = "Task 1",
                        details = "",
                        isCompleted = true,
                        date = 144,
                        time = 144,
                        priority = Priority.MODERATE
                    ),
                    Task(
                        id = 2,
                        title = "Task 2",
                        details = "",
                        date = 144,
                        time = 144,
                        priority = Priority.MODERATE
                    ),
                    Task(
                        id = 3,
                        title = "Task 3",
                        details = "",
                        date = 144,
                        time = 144,
                        priority = Priority.MODERATE
                    ),
                    Task(
                        id = 4,
                        title = "Task 4",
                        details = "",
                        isCompleted = true,
                        date = 144,
                        time = 144,
                        priority = Priority.MODERATE
                    ),
                )
            ),
            onTaskClick = {},
            onTaskDismissed = {},
            onTaskChecked = { _, _ -> }
        )
    }


}

@Preview
@Composable
private fun NoTasksPreview() {
    InjaazTheme {
        TasksScreen(
            tasksUiState = TasksUiState.Empty,
            onTaskClick = {},
            onTaskDismissed = {},
            onTaskChecked = { _, _ -> })
    }
}

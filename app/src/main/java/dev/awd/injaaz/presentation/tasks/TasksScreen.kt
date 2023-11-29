package dev.awd.injaaz.presentation.tasks

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.presentation.components.InjaazSearchBar
import dev.awd.injaaz.ui.theme.InjaazTheme

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    onTaskClick: (Int) -> Unit
) {
    val tasks = listOf(
        Task(
            title = "Task1",
            description = "This is Task one description",
            date = "25 Jan",
            isCompleted = true,
            priority = Priority.HIGH
        ),
        Task(
            title = "Task2",
            description = "This is Task Two description",
            date = "25 Jan",
            isCompleted = true,
            priority = Priority.MODERATE
        ),
        Task(
            title = "Task Test",
            description = "This is Task one description",
            date = "25 Jan",
            isCompleted = false,
            priority = Priority.HIGH
        )
    )
    if (tasks.isEmpty()) NoTasksView(modifier = modifier)
    else {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            InjaazSearchBar(onValueChanged = {}, onFilter = {})
            TaskView(tasks = tasks, onTaskClick = onTaskClick, onTaskChecked = {})
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
            text = "No Tasks yet, Add your first one?".uppercase(),
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
fun TaskView(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onTaskClick: (Int) -> Unit,
    onTaskChecked: (Boolean) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            Text(
                text = "All Tasks",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        items(tasks) {
            TaskItem(
                title = it.title,
                isCompleted = it.isCompleted,
                onTaskClick = { onTaskClick(it.hashCode()) },
                onTaskChecked = onTaskChecked
            )
        }
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
        Text(text = title, color = Color.White, modifier = Modifier.clickable { onTaskClick() })
        Spacer(modifier = Modifier
            .weight(1f)
            .clickable { onTaskClick() })
        IconButton(
            onClick = { onTaskChecked(false) },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isCompleted) R.drawable.tickcircle else R.drawable.emptycircle),
                contentDescription = null,
            )
        }
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
            text = "Completed Tasks",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
        )
        LazyRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            items(tasks) { task ->
                TasksRowItem(title = task.title, date = task.date)
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
                text = "Ongoing Tasks",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        items(tasks) {

        }
    }
}

@Composable
fun TaskColumnItem(
    modifier: Modifier = Modifier,
    title: String,
    date: String
) {

}

@Preview
@Composable
private fun TasksPreview() {
    InjaazTheme {
        TasksScreen(
            modifier = Modifier.padding(8.dp),
            onTaskClick = {},
        )
    }

}
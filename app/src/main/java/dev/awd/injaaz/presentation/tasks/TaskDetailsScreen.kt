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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.ui.theme.pilat_extended

@Composable
fun TaskDetailsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    task: Task
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
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.minimumInteractiveComponentSize()
            )
        })
        Text(
            text = task.title,
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
            PropertyView(icon = R.drawable.calendar, title = "Due Date", subtitle = task.date)
            PropertyView(
                icon = R.drawable.priority,
                title = "Priority",
                subtitle = task.priority.name
            )
        }
        Text(
            text = "Task Details",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = task.description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

    }
}

@Composable
fun PropertyView(
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
        TaskDetailsScreen(
            onBackPressed = { /*TODO*/ }, task = Task(
                title = "Task Title",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled",
                date = "20 Jun",
                priority = Priority.HIGH
            )
        )
    }
}
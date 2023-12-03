package dev.awd.injaaz.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.awd.injaaz.R
import dev.awd.injaaz.presentation.notes.NotesScreen
import dev.awd.injaaz.presentation.tasks.TasksRoute
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.ui.theme.pilat_extended

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String,
    userAvatar: String,
    onAddButtonClick: (Int) -> Unit,
    onUserAvatarClick: () -> Unit,
    onTaskItemClick: (Int) -> Unit,
    onNoteItemClick: (Int) -> Unit,
    ) {

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            InjaazTopBar(
                subtitle = userName,
                icon = userAvatar,
                onIconClick = onUserAvatarClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset(y = 48.dp)
//                    .background(MaterialTheme.colorScheme.primary)
                    .border(
                        width = 8.dp,
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .shadow(shape = CircleShape, elevation = 8.dp, clip = true)
                    .padding(4.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 16.dp
                ),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black,
                onClick = { onAddButtonClick(selectedItemIndex) }) {
                Icon(painter = painterResource(id = R.drawable.add), contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF263238),
                tonalElevation = 4.dp
            ) {
                bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = Color(0xFF263238)
                        ),
                        label = { Text(text = item.first) },
                        onClick = { selectedItemIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.second),
                                contentDescription = null
                            )
                        })
                }

            }
        }

    ) { paddingValues ->
        when (selectedItemIndex) {
            0 -> TasksRoute(
                modifier = modifier.padding(paddingValues),
                onTaskClick = onTaskItemClick,
            )

            1 -> NotesScreen(
                modifier = modifier.padding(paddingValues),
                onNoteClick = onNoteItemClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InjaazTopBar(
    modifier: Modifier = Modifier,
    title: String = "Welcome Back!",
    subtitle: String,
    icon: String,
    onIconClick: () -> Unit
) {
    TopAppBar(modifier = modifier,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent
        ), title = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = pilat_extended,
                    fontWeight = FontWeight(600),
                    color = Color.White
                )

            }
        },
        actions = {
            SubcomposeAsyncImage(
                model = icon,
                contentDescription = subtitle,
                contentScale = ContentScale.FillBounds,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                },
                error = {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(12.dp)
                    .size(48.dp)
                    .shadow(shape = CircleShape, elevation = 8.dp, clip = true)
                    .padding(4.dp)
                    .clickable { onIconClick() }
            )
        })


}

@Preview
@Composable
private fun HomePreview() {
    InjaazTheme {
        HomeScreen(
            userName = "Mahmoud Awad",
            userAvatar = "",
            onUserAvatarClick = {},
            onAddButtonClick = {},
            onTaskItemClick = {},
            onNoteItemClick = {})
    }
}

private val bottomNavigationItems = listOf(
    "Tasks" to R.drawable.tasks,
    "Notes" to R.drawable.edit
)
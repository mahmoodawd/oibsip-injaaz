package dev.awd.injaaz.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.awd.injaaz.R
import dev.awd.injaaz.presentation.components.InjaazBottomBar
import dev.awd.injaaz.presentation.notes.noteslist.NotesScreen
import dev.awd.injaaz.presentation.tasks.tasklist.TasksScreen
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.ui.theme.pilat_extended


/**
 * Stateful Screen for Home Content
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String,
    userAvatar: String,
    onAddButtonClick: (currentScreen: BottomBarScreen) -> Unit,
    onUserAvatarClick: () -> Unit,
    onTaskLongClick: (taskId: Int) -> Unit,
    onNoteItemClick: (noteId: Int) -> Unit,
) {
    val bottomNavigationItems = BottomBarScreen.entries
    var currentScreen by rememberSaveable {
        mutableStateOf(BottomBarScreen.Tasks)
    }

    @Composable
    fun ScreenBody(): @Composable (PaddingValues) -> Unit =
        when (currentScreen) {
            BottomBarScreen.Tasks -> { paddingValues ->
                TasksScreen(
                    modifier.padding(paddingValues),
                    onTaskLongClick = onTaskLongClick,
                )
            }

            BottomBarScreen.Notes -> { paddingValues ->
                NotesScreen(
                    modifier.padding(paddingValues),
                    onNoteClick = onNoteItemClick,
                )
            }
        }



    HomeScreen(
        userName = userName,
        userAvatar = userAvatar,
        onAddButtonClick = onAddButtonClick,
        onUserAvatarClick = onUserAvatarClick,
        bottomNavigationItems = bottomNavigationItems,
        currentScreen = currentScreen,
        currentScreenBody = ScreenBody(),
        onScreenChange = { screen -> currentScreen = screen }
    )
}

/**
 * Stateless Screen for Home Content
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String,
    userAvatar: String,
    onAddButtonClick: (currentScreen: BottomBarScreen) -> Unit,
    onUserAvatarClick: () -> Unit,
    bottomNavigationItems: List<BottomBarScreen>,
    currentScreen: BottomBarScreen,
    currentScreenBody: @Composable (PaddingValues) -> Unit,
    onScreenChange: (BottomBarScreen) -> Unit,
) {


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
            AddNewFab(
                onClick = { onAddButtonClick(currentScreen) },
                label = stringResource(
                    id = when (currentScreen) {
                        BottomBarScreen.Tasks -> R.string.new_task
                        BottomBarScreen.Notes -> R.string.new_note
                    }
                )
            )
        },
        bottomBar = {
            InjaazBottomBar(
                currentScreen = currentScreen,
                screens = bottomNavigationItems,
                onTabSelected = onScreenChange
            )
        }, content = currentScreenBody
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InjaazTopBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.welcome_back),
    subtitle: String,
    icon: String,
    onIconClick: () -> Unit,
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


@Composable
fun AddNewFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
) {
    FloatingActionButton(
        modifier = modifier
            .offset(y = 48.dp)
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
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = label
        )
    }
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
            onTaskLongClick = {},
            onNoteItemClick = {})
    }
}


enum class BottomBarScreen(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
) {
    Tasks(
        title = R.string.tasks,
        icon = R.drawable.tasks,
    ),

    Notes(
        title = R.string.notes,
        icon = R.drawable.edit
    )
}
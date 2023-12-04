package dev.awd.injaaz

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.awd.injaaz.presentation.HomeScreen
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.presentation.auth.GoogleAuthUiClient
import dev.awd.injaaz.presentation.auth.WelcomeScreen
import dev.awd.injaaz.presentation.notes.notesdetails.NoteDetailsRoute
import dev.awd.injaaz.presentation.settings.SettingsScreen
import dev.awd.injaaz.presentation.tasks.NewTaskRoute
import dev.awd.injaaz.presentation.tasks.TaskDetailsRoute
import dev.awd.injaaz.ui.theme.InjaazTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleAuthUiClient: AuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InjaazTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InjaazNavHost(navController, googleAuthUiClient)
                }
            }
        }
    }
}

@Composable
fun InjaazNavHost(
    navController: NavHostController,
    googleAuthUiClient: AuthUiClient,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUser = googleAuthUiClient.getSignedInUser()
    var startDestination by remember {
        mutableStateOf(WelcomeDest.route)
    }

    //Auto Login
    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            startDestination = HomeDest.route
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = WelcomeDest.route) {
            WelcomeScreen(modifier = modifier,
                googleAuthUiClient = googleAuthUiClient,
                onEmailButtonClick = {
                    Toast.makeText(context, "SOON", Toast.LENGTH_SHORT).show()
                }, onSignInSuccess = {
                    navController.navigate(HomeDest.route)
                })
        }
        composable(route = HomeDest.route) {
            HomeScreen(userName = currentUser?.userName ?: "",
                userAvatar = currentUser?.profilePhotoUrl ?: "",
                onAddButtonClick = { screenIndex ->
                    when (screenIndex) {
                        0 -> navController.navigate(NewTaskDest.route)
                        1 -> navController.navigateToNoteDetails(-1)
                    }

                },
                onUserAvatarClick = { navController.navigate(SettingsDest.route) },
                onTaskItemClick = {
                    navController.navigateToTaskDetails(it)
                },
                onNoteItemClick = { navController.navigateToNoteDetails(it) })
        }
        composable(route = NewTaskDest.route) {
            NewTaskRoute(onBackPressed = { navController.popBackStack() })
        }
        composable(
            route = TaskDetailsDest.routeWithArgs,
            arguments = TaskDetailsDest.arguments
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(TaskDetailsDest.taskIdArg) ?: 0

            TaskDetailsRoute(
                taskId = id,
                onBackPressed = { navController.popBackStack() })
        }

        composable(
            route = NewNoteDest.routeWithArgs,
            arguments = NewNoteDest.arguments
        ) {
            NoteDetailsRoute { navController.popBackStack() }
        }
        composable(route = SettingsDest.route) {
            SettingsScreen(googleAuthUiClient = googleAuthUiClient, onLogoutSuccess = {
                navController.navigate(WelcomeDest.route)
            }, onBackPressed = { navController.popBackStack() })
        }

    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun GreetingPreview() {
    InjaazTheme {
        InjaazNavHost(
            rememberNavController(),
            GoogleAuthUiClient(LocalContext.current)
        )
    }
}

fun NavController.navigateToTaskDetails(taskId: Int) {
    navigate("${TaskDetailsDest.route}/$taskId")
}

fun NavController.navigateToNoteDetails(noteId: Int) {
    navigate("${NewNoteDest.route}/$noteId")
}
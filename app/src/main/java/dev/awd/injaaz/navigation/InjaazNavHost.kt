package dev.awd.injaaz.navigation

import android.content.res.Configuration
import android.widget.Toast
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
import dev.awd.injaaz.presentation.HomeScreen
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.presentation.auth.GoogleAuthUiClient
import dev.awd.injaaz.presentation.auth.WelcomeScreen
import dev.awd.injaaz.presentation.notes.notesdetails.NoteDetailsRoute
import dev.awd.injaaz.presentation.settings.SettingsScreen
import dev.awd.injaaz.presentation.tasks.taskdetails.TaskDetailsRoute
import dev.awd.injaaz.ui.theme.InjaazTheme

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
                        0 -> navController.navigateToTaskDetails(-1)
                        1 -> navController.navigateToNoteDetails(-1)
                    }

                },
                onUserAvatarClick = { navController.navigate(SettingsDest.route) },
                onTaskItemClick = { navController.navigateToTaskDetails(it) },
                onNoteItemClick = { navController.navigateToNoteDetails(it) })
        }

        composable(
            route = TaskDetailsDest.routeWithArgs,
            arguments = TaskDetailsDest.arguments,
            content = { TaskDetailsRoute(onBackPressed = { navController.popBackStack() }) }
        )

        composable(
            route = NoteDetailsDest.routeWithArgs,
            arguments = NoteDetailsDest.arguments,
            content = { NoteDetailsRoute { navController.popBackStack() } }
        )
        composable(route = SettingsDest.route) {
            SettingsScreen(googleAuthUiClient = googleAuthUiClient, onLogoutSuccess = {
                navController.navigate(WelcomeDest.route) {
                    popUpTo(WelcomeDest.route) {
                        inclusive = true
                    }
                }
            }, onBackPressed = { navController.popBackStack() })
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
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
    navigate("${NoteDetailsDest.route}/$noteId")
}
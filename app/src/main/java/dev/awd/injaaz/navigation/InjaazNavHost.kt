package dev.awd.injaaz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.awd.injaaz.domain.models.User
import dev.awd.injaaz.presentation.BottomBarScreen
import dev.awd.injaaz.presentation.HomeScreen
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.presentation.auth.WelcomeScreen
import dev.awd.injaaz.presentation.notes.notesdetails.NoteDetailsRoute
import dev.awd.injaaz.presentation.settings.SettingsScreen
import dev.awd.injaaz.presentation.tasks.taskdetails.TaskDetailsScreen
import kotlinx.coroutines.launch

private const val NEW_ITEM_ID = -1

@Composable
fun InjaazNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = WelcomeDest.route,
    googleAuthUiClient: AuthUiClient,
    currentUser: User?,
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = WelcomeDest.route) {
            WelcomeScreen(modifier = modifier,
                googleAuthUiClient = googleAuthUiClient,
                onSignInSuccess = {
                    navController.navigate(HomeDest.route) {
                        popUpTo(WelcomeDest.route) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(route = HomeDest.route) {
            HomeScreen(
                userName = currentUser?.userName ?: "",
                userAvatar = currentUser?.profilePhotoUrl ?: "",
                onAddButtonClick = { screenIndex ->
                    when (screenIndex) {
                        BottomBarScreen.Tasks -> navController.navigateToTaskDetails()
                        BottomBarScreen.Notes -> navController.navigateToNoteDetails()
                    }
                },
                onUserAvatarClick = { navController.navigate(SettingsDest.route) },
                onTaskLongClick = navController::navigateToTaskDetails,
                onNoteItemClick = navController::navigateToNoteDetails,
            )
        }

        composable(
            route = TaskDetailsDest.routeWithArgs,
            arguments = TaskDetailsDest.arguments,
            content = { TaskDetailsScreen(onBackPressed = { navController.popBackStack() }) }
        )

        composable(
            route = NoteDetailsDest.routeWithArgs,
            arguments = NoteDetailsDest.arguments,
            content = { NoteDetailsRoute { navController.popBackStack() } }
        )
        composable(route = SettingsDest.route) {
            SettingsScreen(user = currentUser, onLogout = {
                lifecycleScope.launch {
                    googleAuthUiClient.signOut()
                }
                navController.navigate(WelcomeDest.route) {
                    popUpTo(startDestination) {
                        inclusive = true
                    }
                }
            }, onBackPressed = { navController.popBackStack() })
        }

    }
}

fun NavController.navigateToTaskDetails(taskId: Int = NEW_ITEM_ID) {
    navigate("${TaskDetailsDest.route}/$taskId")
}

fun NavController.navigateToNoteDetails(noteId: Int = NEW_ITEM_ID) {
    navigate("${NoteDetailsDest.route}/$noteId")
}
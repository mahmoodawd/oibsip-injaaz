package dev.awd.injaaz.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}

object WelcomeDest : Destination {
    override val route: String
        get() = "welcome-screen"
}

object LoginDest : Destination {
    override val route: String
        get() = "login-screen"
}

object SignUpDest : Destination {
    override val route: String
        get() = "signup-screen"
}

object ForgetPasswordDest : Destination {
    override val route: String
        get() = "forget-password-screen"
}

object HomeDest : Destination {
    override val route: String
        get() = "home-screen"
}

object SettingsDest : Destination {
    override val route: String
        get() = "settings-screen"
}


object TaskDetailsDest : Destination {
    override val route: String
        get() = "task-details-screen"
    const val taskIdArg: String = "tasksId"
    val routeWithArgs: String = "$route/{$taskIdArg}"
    val arguments = listOf(
        navArgument(taskIdArg) {
            type = NavType.IntType
            defaultValue = -1
        }
    )
}


object NoteDetailsDest : Destination {
    override val route: String
        get() = "note-details-screen"
    const val noteIdArg: String = "noteId"
    val routeWithArgs: String = "$route/{$noteIdArg}"
    val arguments = listOf(
        navArgument(noteIdArg) {
            type = NavType.IntType
            defaultValue = -1
        }
    )
}

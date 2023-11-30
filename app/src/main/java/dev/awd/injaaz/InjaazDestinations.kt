package dev.awd.injaaz

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

object NewTaskDest : Destination {
    override val route: String
        get() = "new-task-screen"
}

object TaskDetailsDest : Destination {
    override val route: String
        get() = "task-details-screen"
}

object NotesDest : Destination {
    override val route: String
        get() = "notes-screen"
}

object NewNoteDest : Destination {
    override val route: String
        get() = "new-note-screen"
}
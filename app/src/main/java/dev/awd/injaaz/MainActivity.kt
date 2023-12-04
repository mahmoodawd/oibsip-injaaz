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
import dev.awd.injaaz.navigation.HomeDest
import dev.awd.injaaz.navigation.InjaazNavHost
import dev.awd.injaaz.navigation.NoteDetailsDest
import dev.awd.injaaz.navigation.SettingsDest
import dev.awd.injaaz.navigation.TaskDetailsDest
import dev.awd.injaaz.navigation.WelcomeDest
import dev.awd.injaaz.presentation.HomeScreen
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.presentation.auth.GoogleAuthUiClient
import dev.awd.injaaz.presentation.auth.WelcomeScreen
import dev.awd.injaaz.presentation.notes.notesdetails.NoteDetailsRoute
import dev.awd.injaaz.presentation.settings.SettingsScreen
import dev.awd.injaaz.presentation.tasks.taskdetails.TaskDetailsRoute
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



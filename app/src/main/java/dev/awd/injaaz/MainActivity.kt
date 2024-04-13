package dev.awd.injaaz

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.awd.injaaz.domain.models.User
import dev.awd.injaaz.navigation.HomeDest
import dev.awd.injaaz.navigation.InjaazNavHost
import dev.awd.injaaz.navigation.WelcomeDest
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.ui.theme.InjaazTheme
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Extends from AppCompatActivity (which requires using an AppCompat theme for the Activity)
 * Otherwise, setApplicationLocales will do nothing.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var googleAuthUiClient: AuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var hideSplashScreen = false
        installSplashScreen()
        /*.setKeepOnScreenCondition {
            !hideSplashScreen
        }*/
        //Here we used setContentView with ComposeView to be able to use setContent in Android Testing
//        setContentView(ComposeView(this).apply {
        setContent {
            InjaazTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InjaazApp(
                        googleAuthUiClient = googleAuthUiClient,
                        onSplashTimeOut = { !hideSplashScreen },
                        currentUser = googleAuthUiClient.getSignedInUser()
                    )
                }
            }
            /* InjaazTheme {
                 val navController = rememberNavController()
                 Surface(
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colorScheme.background
                 ) {
                     InjaazNavHost(
                         navController,
                         googleAuthUiClient,
                         onSplashTimeOut = { hideSplashScreen = true })
                 }
             }
         }*/
//        })
        }
    }
}

@Composable
fun InjaazApp(
    modifier: Modifier = Modifier,
    googleAuthUiClient: AuthUiClient,
    onSplashTimeOut: () -> Unit,
    currentUser: User?,

    ) {
    val startDestination = when (currentUser) {
        null -> WelcomeDest.route
        else -> HomeDest.route
    }


    LaunchedEffect(key1 = Unit) {
        delay(1000L)
        onSplashTimeOut()
    }

    val navController = rememberNavController()

    InjaazNavHost(
        navController = navController,
        googleAuthUiClient = googleAuthUiClient,
        currentUser = currentUser,
        startDestination = startDestination,
        modifier = modifier
    )
}



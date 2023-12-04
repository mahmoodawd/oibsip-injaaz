package dev.awd.injaaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.awd.injaaz.navigation.InjaazNavHost
import dev.awd.injaaz.presentation.auth.AuthUiClient
import dev.awd.injaaz.ui.theme.InjaazTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleAuthUiClient: AuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var hideSplashScreen = false
        installSplashScreen().setKeepOnScreenCondition {
            !hideSplashScreen
        }
        setContent {
            InjaazTheme {
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
        }
    }
}



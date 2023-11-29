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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.awd.injaaz.presentation.WelcomeScreen
import dev.awd.injaaz.ui.theme.InjaazTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InjaazTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InjaazNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun InjaazNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = WelcomeDest.route,
        modifier = modifier
    ) {
        composable(route = WelcomeDest.route) {
            WelcomeScreen(modifier = modifier, onEmailButtonClick = {
                Toast.makeText(context, "SOON", Toast.LENGTH_SHORT).show()
            }, onGoogleButtonClick = {})
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun GreetingPreview() {
    InjaazTheme {
        InjaazNavHost(rememberNavController())
    }
}
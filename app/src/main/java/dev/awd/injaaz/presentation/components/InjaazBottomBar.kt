package dev.awd.injaaz.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.awd.injaaz.presentation.BottomBarScreen


@Composable
fun InjaazBottomBar(
    modifier: Modifier = Modifier,
    currentScreen: BottomBarScreen,
    screens: List<BottomBarScreen>,
    onTabSelected: (screen: BottomBarScreen) -> Unit,
) {

    val navBarColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = Color(0xFF263238)
    )
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF263238),
        tonalElevation = 4.dp
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = stringResource(id = screen.title)) },
                interactionSource = remember { MutableInteractionSource() },
                selected = currentScreen == screen,
                colors = navBarColors,
                onClick = { onTabSelected(screen) },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(id = screen.title)
                    )
                })
        }

    }
}
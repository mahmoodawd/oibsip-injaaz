package dev.awd.injaaz.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.awd.injaaz.R
import dev.awd.injaaz.ui.theme.InjaazTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    screenTitle: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: () -> Unit
) {

    CenterAlignedTopAppBar(modifier = modifier,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent
        ), title = { Text(text = screenTitle, color = Color.White) },
        actions = actions,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowleft),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    )
}

@Preview
@Composable
private fun ScreenHeaderPreview() {
    InjaazTheme {
        ScreenHeader(screenTitle = "Screen Title") {}
    }
}
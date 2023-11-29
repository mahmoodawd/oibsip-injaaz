package dev.awd.injaaz.presentation.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.awd.injaaz.R
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(16.dp)
    ) {

        ScreenHeader(screenTitle = "Settings", onBackPressed = onBackPressed)
        Image(
            painter = painterResource(id = R.drawable.usertag), contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .size(127.dp)
                .shadow(shape = CircleShape, elevation = 8.dp, clip = true)
                .background(MaterialTheme.colorScheme.primary)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(4.dp)
        )
        SettingTile(
            title = "username",
            leadingIcon = R.drawable.useradd,
            trailingIcon = R.drawable.edit
        ) {}
        SettingTile(
            title = "email",
            leadingIcon = R.drawable.usertag,
            trailingIcon = R.drawable.edit
        ) {}
        SettingTile(
            title = "password",
            leadingIcon = R.drawable.lock,
            trailingIcon = R.drawable.edit
        ) {}
        SettingTile(
            title = "Language",
            leadingIcon = R.drawable.settings,
            trailingIcon = R.drawable.arrowdown
        ) {}
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logout), contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(text = "Logout", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingTile(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    onTrailingClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(MaterialTheme.colorScheme.onBackground)
    ) {
        Icon(
            painter = painterResource(id = leadingIcon),
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(8.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = trailingIcon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onTrailingClick() },
        )

    }

}

@Preview
@Composable
private fun SettingsPreview() {
    InjaazTheme {
        SettingsScreen {}
    }
}
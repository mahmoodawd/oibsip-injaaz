package dev.awd.injaaz.presentation.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.User
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onLogout: () -> Unit,
    onBackPressed: () -> Unit,
) {

    var showSheet by remember {
        mutableStateOf(false)
    }
    val viewModel: SettingsViewModel = hiltViewModel()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()


    if (showSheet) {
        LanguageBottomSheet(
            currentLanguage = currentLanguage,
            onDismissRequest = { showSheet = false },
            onLanguageSelected = viewModel::onLanguageChanged,
            onConfirm = viewModel::onConfirmLanguageChanged
        )

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(16.dp)
    ) {

        ScreenHeader(
            screenTitle = stringResource(R.string.settings),
            onBackPressed = onBackPressed
        )
        SubcomposeAsyncImage(
            model = user?.profilePhotoUrl,
            contentDescription = user?.userName,
            loading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.scale(0.5f)
                )
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null
                )
            },
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
            title = user?.userName ?: stringResource(R.string.username),
            leadingIcon = R.drawable.useradd,
            trailingIcon = R.drawable.edit
        ) {}
        SettingTile(
            title = user?.email ?: stringResource(R.string.email),
            leadingIcon = R.drawable.usertag,
            trailingIcon = R.drawable.edit
        ) {}
        SettingTile(
            title = stringResource(R.string.password),
            leadingIcon = R.drawable.lock,
            trailingIcon = R.drawable.edit
        ) {}
        SettingTile(
            title = stringResource(R.string.language),
            leadingIcon = R.drawable.language,
            trailingIcon = R.drawable.arrowdown
        ) {
            showSheet = true
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logout), contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = stringResource(R.string.logout),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun SettingTile(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    onTrailingClick: () -> Unit,
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

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    iconEmoji: String,
    @StringRes title: Int,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = iconEmoji, modifier = Modifier.padding(8.dp))
        Text(text = stringResource(id = title), modifier = Modifier.weight(1f))
        RadioButton(selected = selected, onClick = onClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    modifier: Modifier = Modifier,
    currentLanguage: String,
    onDismissRequest: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        tonalElevation = 4.dp,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,
        onDismissRequest = onDismissRequest
    ) {
        LanguagesList(
            currentLanguage = currentLanguage,
            onLanguageSelected = onLanguageSelected,
            onConfirm = onConfirm
        )
    }
}

@Composable
fun LanguagesList(
    modifier: Modifier = Modifier,
    currentLanguage: String,
    onConfirm: () -> Unit,
    onLanguageSelected: (language: String) -> Unit,
) {
    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Timber.d("Lang: $currentLanguage")
        LanguageItem(
            iconEmoji = US_EMOJI,
            selected = currentLanguage == EN,
            title = R.string.english
        ) {
            onLanguageSelected(EN)
        }
        LanguageItem(
            iconEmoji = EGY_EMOJI,
            selected = currentLanguage == AR,
            title = R.string.arabic
        ) {
            onLanguageSelected(AR)
        }
        Button(
            onClick = onConfirm,
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.confirm),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {

    InjaazTheme {
        SettingsScreen(user = User("", "User", "user@email.com", ""), onLogout = {}) {}
    }
}

private const val AR = "ar-EG"
private const val EN = "en-US"
private const val US_EMOJI = "\uD83C\uDDFA\uD83C\uDDF8"
private const val EGY_EMOJI = "\uD83C\uDDEA\uD83C\uDDEC"


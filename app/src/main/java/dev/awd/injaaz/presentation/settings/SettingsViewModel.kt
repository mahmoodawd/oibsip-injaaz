package dev.awd.injaaz.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.text.intl.Locale
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    var currentLanguage = MutableStateFlow(getCurrentLanguage())
        private set

    fun onLanguageChanged(language: String) {
        currentLanguage.update { language }
    }

    fun onConfirmLanguageChanged() {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(
                currentLanguage.value
            )
        )
    }

    private fun getCurrentLanguage() = AppCompatDelegate
        .getApplicationLocales().toLanguageTags()
        .ifEmpty { Locale.current.toLanguageTag() }

}
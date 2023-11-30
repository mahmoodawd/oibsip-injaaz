package dev.awd.injaaz.presentation.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber


class AuthViewModel : ViewModel() {
    fun onSignInResult(result: SignInResult) {
        Timber.d("Called")
        _uiState.update {
            it.copy(
                isSuccessful = result.user != null,
                isError = result.errorMessage
            )
        }
        Timber.i(result.toString())
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()


    fun resetState() {
        _uiState.update { AuthUiState() }
    }

}
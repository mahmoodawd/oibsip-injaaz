package dev.awd.injaaz.presentation.auth

data class AuthUiState(
    val isSuccessful: Boolean = false,
    val isError: String? = null
)
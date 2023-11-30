package dev.awd.injaaz.presentation.auth

data class UserData(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePhotoUrl: String
)
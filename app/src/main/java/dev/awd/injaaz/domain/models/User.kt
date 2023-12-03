package dev.awd.injaaz.domain.models

data class User(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePhotoUrl: String
)
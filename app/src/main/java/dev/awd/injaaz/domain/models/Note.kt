package dev.awd.injaaz.domain.models

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val timeStamp: Long,
)
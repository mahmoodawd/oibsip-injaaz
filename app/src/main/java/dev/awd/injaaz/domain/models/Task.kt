package dev.awd.injaaz.domain.models

data class Task(
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val date: String,
    val priority: Priority,
)

enum class Priority {
    HIGH, MODERATE, LOW
}
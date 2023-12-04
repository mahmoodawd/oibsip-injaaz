package dev.awd.injaaz.domain.models


data class Task(
    val id: Int = 0,
    var title: String,
    var details: String,
    var isCompleted: Boolean = false,
    var date: Long,
    var time: Int,
    var priority: Priority,
)

enum class Priority {
    HIGH, MODERATE, LOW
}
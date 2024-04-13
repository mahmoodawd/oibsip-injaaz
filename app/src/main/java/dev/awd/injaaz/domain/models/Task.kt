package dev.awd.injaaz.domain.models

import androidx.annotation.StringRes
import dev.awd.injaaz.R


data class Task(
    val id: Int = 0,
    var title: String,
    var details: String,
    var isCompleted: Boolean = false,
    var date: Long,
    var time: Long,
    var priority: Priority,
)

enum class Priority(@StringRes val title: Int) {
    HIGH(R.string.priority_high), MODERATE(R.string.priority_moderate), LOW(R.string.priority_low)
}
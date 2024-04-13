package dev.awd.injaaz.data.mappers

import dev.awd.injaaz.data.local.entities.TaskEntity
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task

fun TaskEntity.toTask() =
    Task(
        id = id,
        title = title,
        details = description,
        isCompleted = isCompleted,
        priority = Priority.entries.firstOrNull { it.name == this@toTask.priority }
            ?: Priority.MODERATE,
        date = date,
        time = time
    )

fun Task.toTaskEntity(userId: String) =
    TaskEntity(
        id = id,
        userId = userId,
        title = title,
        description = details,
        date = date,
        time = time,
        isCompleted = isCompleted,
        priority = priority.name
    )
package dev.awd.injaaz.presentation.tasks.tasklist

import dev.awd.injaaz.domain.models.Task

sealed interface TasksUiState {
    data object Loading : TasksUiState

    data class Tasks(
        val tasks: List<Task>,
    ) : TasksUiState

    data object Empty : TasksUiState
}
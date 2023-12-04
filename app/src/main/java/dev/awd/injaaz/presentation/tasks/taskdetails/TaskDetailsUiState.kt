package dev.awd.injaaz.presentation.tasks.taskdetails

import dev.awd.injaaz.domain.models.Priority

sealed interface TaskDetailsUiState {
    data object Loading : TaskDetailsUiState

    data class TaskDetails(
        val taskTitle: String = "",
        val taskDetails: String = "",
        val taskDate: Long = 0L,
        val taskTime: Int = 0,
        val taskPriority: Priority = Priority.MODERATE,
    ) : TaskDetailsUiState

    data class Error(
        val msg: String
    ) : TaskDetailsUiState
}
package dev.awd.injaaz.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.domain.repository.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {
    private val taskId = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val taskDetailsUiStateState: StateFlow<TaskDetailsUiState> = taskId.flatMapLatest {
        tasksRepository.getTaskById(it).mapLatest { result ->
            Timber.d(result.toString())
            when (result) {
                is Result.Success<*> -> TaskDetailsUiState.TaskDetails(result.data as Task)
                is Result.Failure -> TaskDetailsUiState.Error(result.error)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TaskDetailsUiState.Loading
    )

    fun getTaskDetails(id: Int) {
        taskId.value = id
    }


}


sealed interface TaskDetailsUiState {
    data object Loading : TaskDetailsUiState

    data class TaskDetails(
        val task: Task,
    ) : TaskDetailsUiState

    data class Error(
        val msg: String
    ) : TaskDetailsUiState
}


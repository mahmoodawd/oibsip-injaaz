package dev.awd.injaaz.presentation.tasks.tasklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.awd.injaaz.R
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.domain.repository.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TASKS_KEY = "tasks"

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tasks = savedStateHandle.getStateFlow(TASKS_KEY, emptyList<Task>())
    val tasksEffect = MutableSharedFlow<Int>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksUiState: StateFlow<TasksUiState> =
        tasks.onEach {
            TasksUiState.Loading
        }.mapLatest {
            if (it.isEmpty())
                TasksUiState.Empty
            else
                TasksUiState.Tasks(tasks = it)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
                //Resets the cache immediately after stopping the flow
                initialValue = TasksUiState.Loading
            )

    fun loadTasks() {
        viewModelScope.launch {

            tasksRepository.getAllTasks<List<Task>>().collectLatest { result ->
                savedStateHandle[TASKS_KEY] = when (result) {

                    is Result.Success -> result.data as List<Task>

                    is Result.Failure -> emptyList()
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.deleteTask(task)
            tasksEffect.emit(R.string.task_deleted)
            loadTasks()
        }
    }

    fun updateTask(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            tasksRepository.updateTask(task.copy(isCompleted = isCompleted))
        }
    }


}
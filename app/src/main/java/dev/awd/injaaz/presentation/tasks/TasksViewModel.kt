package dev.awd.injaaz.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.domain.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {


    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksUiState: StateFlow<TasksUiState> =
        tasksRepository.getAllTasks().mapLatest { result ->
            when (result) {
                is Result.Success<*> -> {
                    val tasks = result.data as List<Task>
                    if (tasks.isEmpty()) TasksUiState.Empty
                    else {
                        tasks.forEach { Timber.i(it.title + it.isCompleted) }
                        TasksUiState.Tasks(tasks = tasks.sortedByDescending { it.date })
                    }
                }

                is Result.Failure -> TasksUiState.Empty
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(replayExpirationMillis = 200),
            initialValue = TasksUiState.Loading
        )

    fun addNewTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.createNewTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.deleteTask(task)
        }
    }

    fun updateTask(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            tasksRepository.updateTask(task.copy(isCompleted = isCompleted))
        }
    }


}
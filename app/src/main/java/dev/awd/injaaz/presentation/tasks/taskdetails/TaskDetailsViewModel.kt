package dev.awd.injaaz.presentation.tasks.taskdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.awd.injaaz.navigation.TaskDetailsDest
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.domain.repository.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


private const val TASK_TITLE_KEY = "taskTitle"
private const val TASK_DETAILS_KEY = "taskDetails"
private const val TASK_DATE_KEY = "taskDate"
private const val TASK_TIME_KEY = "taskTime"
private const val TASK_PRIORITY_KEY = "taskPriority"

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val calendar = Calendar.getInstance()
    private val taskTitle = savedStateHandle.getStateFlow(TASK_TITLE_KEY, "")
    private val taskDetails = savedStateHandle.getStateFlow(TASK_DETAILS_KEY, "")
    private val taskDate =
        savedStateHandle.getStateFlow(TASK_DATE_KEY, calendar.timeInMillis)
    private val taskTime =
        savedStateHandle.getStateFlow(TASK_TIME_KEY, calendar.get(Calendar.HOUR_OF_DAY))
    private val taskPriority = savedStateHandle.getStateFlow(TASK_PRIORITY_KEY, Priority.MODERATE)

    var hasNoteBeenSaved = MutableStateFlow(false)
        private set
    var isCreateButtonEnabled = MutableStateFlow(false)
        private set

    val taskDetailsUiState =
        combine(
            taskTitle,
            taskDetails,
            taskDate,
            taskTime,
            taskPriority
        ) { title, details, date, time, priority ->
            isCreateButtonEnabled.value = title.isNotBlank()
            calendar.set(Calendar.HOUR_OF_DAY, time)
            TaskDetailsUiState.TaskDetails(
                taskTitle = title,
                taskDetails = details,
                taskDate = date,
                taskTime = time,
                taskPriority = priority
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TaskDetailsUiState.Loading
        )

    private var existingTaskId: Int? = null

    init {
        savedStateHandle.get<Int>(TaskDetailsDest.taskIdArg)?.let { existingTaskId ->
            if (existingTaskId == -1) {
                return@let
            }
            this.existingTaskId = existingTaskId
        }
        viewModelScope.launch {
            existingTaskId?.let {
                tasksRepository.getTaskById<Task>(it).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            val task = result.data as Task
                            savedStateHandle[TASK_TITLE_KEY] = task.title
                            savedStateHandle[TASK_DETAILS_KEY] = task.details
                            savedStateHandle[TASK_DATE_KEY] = task.date
                            savedStateHandle[TASK_TIME_KEY] = task.time
                            savedStateHandle[TASK_PRIORITY_KEY] = task.priority
                        }

                        is Result.Failure -> {}
                    }
                }
            }
        }
    }

    fun onTaskTitleChanged(text: String) {
        savedStateHandle[TASK_TITLE_KEY] = text
    }

    fun onTaskDetailsChanged(text: String) {
        savedStateHandle[TASK_DETAILS_KEY] = text
    }

    fun onTaskDateChanged(date: Long) {
        savedStateHandle[TASK_DATE_KEY] = date
    }

    fun onTaskTimeChanged(time: Int) {
        savedStateHandle[TASK_TIME_KEY] = time
    }

    fun onTaskPriorityChanged(priority: Priority) {
        savedStateHandle[TASK_PRIORITY_KEY] = priority
    }

    fun saveTask() {
        viewModelScope.launch {
            if (existingTaskId == null) {

                tasksRepository.createNewTask(
                    Task(
                        title = taskTitle.value,
                        details = taskDetails.value,
                        date = taskDate.value,
                        time = taskTime.value,
                        priority = taskPriority.value,
                        isCompleted = false
                    )
                )
            } else {
                tasksRepository.updateTask(
                    Task(
                        id = existingTaskId!!,
                        title = taskTitle.value,
                        details = taskDetails.value,
                        date = taskDate.value,
                        time = taskTime.value,
                        priority = taskPriority.value,
                    )
                )
            }
            hasNoteBeenSaved.value = true
        }
    }

}

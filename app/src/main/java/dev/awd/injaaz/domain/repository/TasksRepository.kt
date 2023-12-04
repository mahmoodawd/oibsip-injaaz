package dev.awd.injaaz.domain.repository

import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun <T> getAllTasks(): Flow<Result<T>>

    suspend fun createNewTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun <T> getTaskById(taskId: Int): Flow<Result<T>>
}
package dev.awd.injaaz.domain.repository

import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getAllTasks(): Flow<Result>

    suspend fun createNewTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun getTaskById(taskId: Int): Flow<Result>
}
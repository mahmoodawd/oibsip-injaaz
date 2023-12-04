package dev.awd.injaaz.data.repository

import com.google.firebase.auth.FirebaseAuth
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.data.local.dao.TasksDao
import dev.awd.injaaz.data.mappers.toTask
import dev.awd.injaaz.data.mappers.toTaskEntity
import dev.awd.injaaz.domain.models.Task
import dev.awd.injaaz.domain.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class TasksRepoImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val firebaseAuth: FirebaseAuth
) : TasksRepository {

    override fun <T> getAllTasks(): Flow<Result<T>> = flow {
        emit(
            try {
                val userTasks = tasksDao.getAllTask(firebaseAuth.uid ?: "")
                    .map { it.toTask() }

                Result.Success(userTasks as T)

            } catch (e: Exception) {
                Result.Failure(e.message ?: "Unknown Error")
            })
    }.flowOn(Dispatchers.IO)

    override suspend fun createNewTask(task: Task) =
        try {
            tasksDao.addNewTask(task.toTaskEntity(firebaseAuth.uid!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    override suspend fun deleteTask(task: Task) =
        try {
            tasksDao.deleteTask(task.toTaskEntity(firebaseAuth.uid!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    override suspend fun updateTask(task: Task) =
        try {
            val taskEntity = task.toTaskEntity(firebaseAuth.uid!!)
            tasksDao.updateTask(taskEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    override suspend fun <T> getTaskById(taskId: Int): Flow<Result<T>> = flowOf(
        try {
            Result.Success(
                tasksDao.getTaskById(taskId).toTask() as T
            )
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Unknown Error")
        }
    ).flowOn(Dispatchers.IO)


}
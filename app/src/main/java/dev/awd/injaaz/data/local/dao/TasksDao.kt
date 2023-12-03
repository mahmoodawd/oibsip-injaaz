package dev.awd.injaaz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.awd.injaaz.data.local.entities.TaskEntity

@Dao
interface TasksDao {

    @Query("SELECT * FROM TASKS WHERE userId= :userId")
    suspend fun getAllTask(userId: String): List<TaskEntity>

    @Query("SELECT * FROM TASKS WHERE id= :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity

    @Insert
    suspend fun addNewTask(taskEntity: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)
}
package dev.awd.injaaz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.awd.injaaz.data.local.dao.TasksDao
import dev.awd.injaaz.data.local.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class InjaazDb : RoomDatabase() {
    abstract val tasksDao: TasksDao
}
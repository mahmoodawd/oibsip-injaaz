package dev.awd.injaaz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TASKS")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val title: String,
    val date: Long,
    val description: String,
    val isCompleted: Boolean = false,
    val priority: String
)

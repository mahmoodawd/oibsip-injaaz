package dev.awd.injaaz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.awd.injaaz.data.local.entities.NoteEntity

@Dao
interface NotesDao {

    @Query("SELECT * FROM NOTES WHERE userId= :userId")
    suspend fun getAllNotes(userId: String): List<NoteEntity>

    @Query("SELECT * FROM NOTES WHERE id= :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity

    @Insert
    suspend fun addNewNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)
}
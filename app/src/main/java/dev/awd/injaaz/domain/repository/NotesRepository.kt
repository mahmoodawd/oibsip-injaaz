package dev.awd.injaaz.domain.repository

import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun <T>getAllNotes(): Flow<Result<T>>

    suspend fun createNewNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun<T> getNoteById(noteId: Int): Flow<Result<T>>
}
package dev.awd.injaaz.data.repository

import com.google.firebase.auth.FirebaseAuth
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.data.local.dao.NotesDao
import dev.awd.injaaz.data.mappers.toNote
import dev.awd.injaaz.data.mappers.toNoteEntity
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepoImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val firebaseAuth: FirebaseAuth
) : NotesRepository {
    @Suppress("UNCHECKED_CAST")
    override fun <T> getAllNotes(): Flow<Result<T>> = flow {
        emit(
            try {
                val userNotes = notesDao.getAllNotes(firebaseAuth.uid ?: "")
                    .map { it.toNote() }

                Result.Success(userNotes as T)

            } catch (e: Exception) {
                Result.Failure(e.message ?: "Unknown Error")
            })
    }.flowOn(Dispatchers.IO)

    override suspend fun createNewNote(note: Note) {
        try {
            notesDao.addNewNote(note.toNoteEntity(firebaseAuth.uid!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteNote(note: Note) {
        try {
            withContext(Dispatchers.IO) {
                notesDao.deleteNote(note.toNoteEntity(firebaseAuth.uid!!))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun updateNote(note: Note) {
        try {
            notesDao.updateNote(note.toNoteEntity(firebaseAuth.uid!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> getNoteById(noteId: Int): Flow<Result<T>> = flowOf(
        try {
            Result.Success(
                notesDao.getNoteById(noteId).toNote() as T
            )
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Unknown Error")
        }
    ).flowOn(Dispatchers.IO)


}
package dev.awd.injaaz.data.mappers

import dev.awd.injaaz.data.local.entities.NoteEntity
import dev.awd.injaaz.domain.models.Note

fun NoteEntity.toNote() =
    Note(
        id = id,
        title = title,
        content = content,
        timeStamp = timeStamp
    )

fun Note.toNoteEntity(userId: String) =
    NoteEntity(
        id = id,
        userId = userId,
        title = title,
        content = content,
        timeStamp = timeStamp
    )
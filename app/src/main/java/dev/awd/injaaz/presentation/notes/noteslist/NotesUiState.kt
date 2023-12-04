package dev.awd.injaaz.presentation.notes.noteslist

import dev.awd.injaaz.domain.models.Note

sealed interface NotesUiState {
    data object Loading : NotesUiState

    data class Notes(
        val notes: List<Note>,
    ) : NotesUiState

    data object Empty : NotesUiState
}
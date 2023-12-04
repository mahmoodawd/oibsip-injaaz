package dev.awd.injaaz.presentation.notes.notesdetails

sealed interface NoteDetailsUiState {
    data object Loading : NoteDetailsUiState

    data class NoteDetails(
        val noteTitle: String = "",
        val noteContent: String = "",
    ) : NoteDetailsUiState

    data class Error(
        val msg: String
    ) : NoteDetailsUiState
}
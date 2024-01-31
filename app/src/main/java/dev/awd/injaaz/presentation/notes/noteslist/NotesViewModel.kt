package dev.awd.injaaz.presentation.notes.noteslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.awd.injaaz.R
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.domain.repository.NotesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOTES_KEY = "notes"

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val notes = savedStateHandle.getStateFlow(NOTES_KEY, emptyList<Note>())
    val notesEffect = MutableSharedFlow<Int>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val notesUiState: StateFlow<NotesUiState> =
        notes.onEach {
            NotesUiState.Loading
        }.mapLatest {
            if (it.isEmpty())
                NotesUiState.Empty
            else
                NotesUiState.Notes(notes = it)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(replayExpirationMillis = 200),
                initialValue = NotesUiState.Loading
            )


    fun loadNotes() {
        viewModelScope.launch {
            notesRepository.getAllNotes<List<Note>>().collectLatest { result ->
                savedStateHandle[NOTES_KEY] = when (result) {
                    is Result.Success -> result.data as List<Note>

                    is Result.Failure -> emptyList()
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
            notesEffect.emit(R.string.note_deleted)
            loadNotes()
        }
    }

}
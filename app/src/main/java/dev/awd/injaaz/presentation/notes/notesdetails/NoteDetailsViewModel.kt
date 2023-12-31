package dev.awd.injaaz.presentation.notes.notesdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.awd.injaaz.navigation.NoteDetailsDest
import dev.awd.injaaz.data.Result
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

private const val NOTE_TITLE_KEY = "noteTitle"
private const val NOTE_CONTENT_KEY = "noteContent"

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteTitle = savedStateHandle.getStateFlow(NOTE_TITLE_KEY, "")
    private val noteContent = savedStateHandle.getStateFlow(NOTE_CONTENT_KEY, "")

    var hasNoteBeenSaved = MutableStateFlow(false)
        private set
    var isCreateButtonEnabled = MutableStateFlow(false)
        private set

    val noteDetailsUiState = combine(noteTitle, noteContent) { title, content ->
        isCreateButtonEnabled.value = title.isNotBlank() || content.isNotBlank()
        NoteDetailsUiState.NoteDetails(
            noteTitle = title,
            noteContent = content,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = NoteDetailsUiState.Loading
    )

    private var existingNoteId: Int? = null

    init {
        savedStateHandle.get<Int>(NoteDetailsDest.noteIdArg)?.let { existingNoteId ->
            if (existingNoteId == -1) {
                return@let
            }
            this.existingNoteId = existingNoteId
        }
        viewModelScope.launch {
            existingNoteId?.let {
                notesRepository.getNoteById<Note>(it).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            val note = result.data as Note
                            savedStateHandle[NOTE_TITLE_KEY] = note.title
                            savedStateHandle[NOTE_CONTENT_KEY] = note.content
                        }

                        is Result.Failure -> {}
                    }
                }
            }
        }
    }

    fun onNoteTitleChanged(text: String) {
        savedStateHandle[NOTE_TITLE_KEY] = text
    }

    fun onNoteContentChanged(text: String) {
        savedStateHandle[NOTE_CONTENT_KEY] = text
    }

    fun saveNote() {
        viewModelScope.launch {
            if (existingNoteId == null) {

                notesRepository.createNewNote(
                    Note(
                        title = noteTitle.value.ifBlank { noteContent.value.substringBefore(" ") },
                        content = noteContent.value,
                        timeStamp = Calendar.getInstance().timeInMillis
                    )
                )
            } else {
                notesRepository.updateNote(
                    Note(
                        id = existingNoteId!!,
                        title = noteTitle.value.ifBlank { noteContent.value.substringBefore(" ") },
                        content = noteContent.value,
                        timeStamp = Calendar.getInstance().timeInMillis
                    )
                )
            }
            hasNoteBeenSaved.value = true
        }
    }

}

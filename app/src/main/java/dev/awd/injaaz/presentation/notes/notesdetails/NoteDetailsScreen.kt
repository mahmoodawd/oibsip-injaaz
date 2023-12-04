package dev.awd.injaaz.presentation.notes.notesdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.awd.injaaz.R
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme

@Composable
fun NoteDetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: NoteDetailsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val uiState by viewModel.noteDetailsUiState.collectAsStateWithLifecycle()
    val isCreateButtonEnabled by viewModel.isCreateButtonEnabled.collectAsStateWithLifecycle()
    val hasNoteBeenSaved by viewModel.hasNoteBeenSaved.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = hasNoteBeenSaved) {
        if (hasNoteBeenSaved) onBackPressed()
    }

    NoteDetailsScreen(
        state = uiState,
        isCreateButtonEnable = isCreateButtonEnabled,
        onTitleChanged = viewModel::onNoteTitleChanged,
        onContentChanged = viewModel::onNoteContentChanged,
        onAddNote = viewModel::saveNote,
        onBackPressed = onBackPressed,
        modifier = modifier
    )


}

@Composable
fun NoteDetailsScreen(
    modifier: Modifier = Modifier,
    state: NoteDetailsUiState,
    isCreateButtonEnable: Boolean,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onAddNote: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(screenTitle = "Note Details", onBackPressed = onBackPressed, actions = {
            Icon(
                painter = painterResource(id = R.drawable.ticksquare),
                contentDescription = null,
                tint = if (isCreateButtonEnable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = modifier.clickable(isCreateButtonEnable) {
                    onAddNote()
                })
        })
        Spacer(modifier = Modifier.height(4.dp))
        when (state) {
            is NoteDetailsUiState.Error -> {}
            NoteDetailsUiState.Loading -> CircularProgressIndicator()
            is NoteDetailsUiState.NoteDetails -> {

                CustomEditText(
                    text = state.noteTitle,
                    fontSize = 24.sp,
                    hint = "Note Title",
                    onValueChanged = onTitleChanged
                )
                CustomEditText(
                    text = state.noteContent,
                    hint = "Some Content...",
                    singleLine = false,
                    onValueChanged = onContentChanged,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 18.sp,
    hint: String,
    singleLine: Boolean = true,
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = text,
        placeholder = { Text(text = hint) },
        onValueChange = onValueChanged,
        singleLine = singleLine,
        textStyle = TextStyle(
            fontSize = fontSize,
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = Color(0xFF6F8793),
            unfocusedPlaceholderColor = Color(0xFF6F8793),
            focusedContainerColor = MaterialTheme.colorScheme.onBackground,
            unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun NewNotePreview() {
    InjaazTheme {
        NoteDetailsScreen(
            state = NoteDetailsUiState.NoteDetails(),
            isCreateButtonEnable = false,
            onTitleChanged = {},
            onContentChanged = {},
            onAddNote = { }) {

        }
    }
}
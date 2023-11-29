package dev.awd.injaaz.presentation.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.presentation.components.ScreenHeader
import dev.awd.injaaz.ui.theme.InjaazTheme
import java.util.Calendar

@Composable
fun NewNoteScreen(
    modifier: Modifier = Modifier,
    onAddNote: (Note) -> Unit = {},
    onBackPressed: () -> Unit
) {
    var noteTitle by rememberSaveable {
        mutableStateOf("")
    }
    var noteContent by rememberSaveable {
        mutableStateOf("")
    }
    val timeStamp = Calendar.getInstance().timeInMillis.toString()

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(screenTitle = "Create New Note", onBackPressed = onBackPressed, actions = {
            Icon(
                painter = painterResource(id = R.drawable.ticksquare),
                contentDescription = null,
                tint = if (noteContent.isBlank()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(noteContent.isNotBlank()) {
                    onAddNote(
                        Note(
                            title = noteTitle.ifBlank { noteContent.substringBefore(" ") },
                            content = noteContent,
                            timeStamp = timeStamp
                        )
                    )
                })
        })
        CustomEditText(hint = "Title", onValueChanged = { noteTitle = it })
        CustomEditText(
            hint = "Note",
            singleLine = false,
            onValueChanged = { noteContent = it },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    hint: String,
    singleLine: Boolean = true,
    onValueChanged: (String) -> Unit
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }
    TextField(
        value = text,
        placeholder = { Text(text = hint) },
        onValueChange = {
            text = it
            onValueChanged(it)
        },
        singleLine = singleLine,
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
            .padding(vertical = 4.dp)
    )
}

@Preview
@Composable
private fun NewNotePreview() {
    InjaazTheme {
        NewNoteScreen(onAddNote = {}) {}
    }
}
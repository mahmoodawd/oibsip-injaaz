package dev.awd.injaaz.presentation.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.domain.models.Priority
import dev.awd.injaaz.presentation.components.InjaazSearchBar
import dev.awd.injaaz.ui.theme.InjaazTheme

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    notes: List<Note>
) {
    if (notes.isEmpty()) NoNotessView(modifier = modifier)
    else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            InjaazSearchBar(onValueChanged = {}, onFilter = {})
            Text(
                text = "All Notes",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
            )
            NotesView(notes = notes)
        }
    }
}

@Composable
fun NoNotessView(
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.welcome_image),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = "No Notes yet, Add your first one?".uppercase(),
            color = Color.White,
            letterSpacing = 4.sp,
            fontSize = 32.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun NotesView(
    modifier: Modifier = Modifier,
    notes: List<Note>
) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Adaptive(150.dp)) {
        items(notes) {
            NoteItem(title = it.title, content = it.content)
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.primary, RectangleShape)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            softWrap = true,
            color = Color.Black,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = content,
            modifier = Modifier.padding(4.dp)
        )
    }

}

@Preview
@Composable
private fun NotesPreview() {
    InjaazTheme {
        NotesScreen(
            modifier = Modifier.padding(16.dp),
            notes = listOf(
                Note(
                    title = "NoteOne ",
                    content = "This is the note content",
                    timeStamp = "",
                ),
                Note(
                    title = "NoteOne ",
                    content = "This is the note content",
                    timeStamp = "",
                ),
                Note(
                    title = "NoteOne ",
                    content = "This is the note content",
                    timeStamp = "",
                ),

                )
        )
    }
}

fun getColorFromPriority(priority: Priority): Color =
    when (priority) {
        Priority.LOW -> Color.Magenta
        Priority.MODERATE -> Color.Cyan
        Priority.HIGH -> Color.Gray
    }

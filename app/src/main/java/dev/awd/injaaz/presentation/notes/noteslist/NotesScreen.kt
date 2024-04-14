package dev.awd.injaaz.presentation.notes.noteslist

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.presentation.components.InjaazSearchBar
import dev.awd.injaaz.ui.theme.InjaazTheme
import kotlinx.coroutines.flow.collectLatest

/**
 * Stateful Screen for Notes Content
 */
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    onNoteClick: (Int) -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),
) {

    val notesUiState by viewModel.notesUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.loadNotes()
    }
    LaunchedEffect(key1 = true) {
        viewModel.notesEffect.collectLatest {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    NotesScreen(
        windowSize = windowSize,
        uiState = notesUiState,
        onNoteClick = onNoteClick,
        onDeleteIconClick = viewModel::deleteNote,
        modifier = modifier
    )
}

/**
 * Stateless Screen for NotesContent
 */
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    uiState: NotesUiState,
    onNoteClick: (Int) -> Unit,
    onDeleteIconClick: (Note) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (uiState) {
            NotesUiState.Loading -> CircularProgressIndicator(Modifier.align(CenterHorizontally))

            NotesUiState.Empty -> {
                if (windowSize == WindowWidthSizeClass.Compact)
                    NoNotesPortrait(modifier = Modifier.weight(1f))
                else
                    NoNotesLandscape(modifier = Modifier.weight(1f))
            }

            is NotesUiState.Notes -> {

                InjaazSearchBar(
                    hint = stringResource(R.string.search_in_notes),
                    onValueChanged = {},
                    onFilter = {})

                Text(
                    text = stringResource(R.string.all_notes),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                )
                NotesView(
                    notes = uiState.notes,
                    onNoteClick = onNoteClick,
                    onDeleteIconClick = onDeleteIconClick
                )
            }

        }
    }
}

@Composable
fun NotesView(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    onDeleteIconClick: (Note) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(150.dp),
        state = rememberLazyStaggeredGridState()
    ) {
        items(notes, key = { it.id }) { note ->
            var isFrameVisible by remember {
                mutableStateOf(false)
            }
            NoteItem(
                title = note.title,
                content = note.content,
                isFrameVisible = isFrameVisible,
                onClick = {
                    if (isFrameVisible) isFrameVisible = false
                    else onNoteClick(note.id)
                },
                onLongClick = { isFrameVisible = true },
                onDeleteIconClick = { onDeleteIconClick(note) })
        }
    }
}

@Composable
fun NoNotesPortrait(
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.welcome_image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.clip(CircleShape)
        )
        Text(
            text = stringResource(R.string.no_notes_yet).uppercase(),
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
fun NoNotesLandscape(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.clip(CircleShape)
        )
        Text(
            text = stringResource(R.string.no_notes_yet).uppercase(),
            color = Color.White,
            letterSpacing = 4.sp,
            fontSize = 32.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    isFrameVisible: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(if (isFrameVisible) 8.dp else 4.dp),
        shape = RectangleShape,
        border = BorderStroke(
            width = if (isFrameVisible) 4.dp else 0.dp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .padding(if (isFrameVisible) 10.dp else 8.dp)
            .combinedClickable(onLongClick = onLongClick, onClick = onClick),
    ) {
        AnimatedVisibility(visible = isFrameVisible) {

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.delete),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable(onClick = onDeleteIconClick)
                )
            }
        }

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
        NotesScreen(uiState = NotesUiState.Notes(
            listOf(
                Note(
                    id = 1,
                    title = "NoteOne ",
                    content = "This is the note content",
                    timeStamp = 123541,
                ),
                Note(
                    id = 2,
                    title = "NoteTwo",
                    content = "This is the note content",
                    timeStamp = 12522,
                ),
                Note(
                    id = 3,
                    title = "NoteThree ",
                    content = "This is the note content",
                    timeStamp = 236,
                )
            )
        ), onNoteClick = {}, onDeleteIconClick = {})
    }
}

@Preview
@Composable
private fun NoNotesPreview() {
    InjaazTheme {
        NotesScreen(uiState = NotesUiState.Empty, onNoteClick = {}, onDeleteIconClick = {})
    }
}

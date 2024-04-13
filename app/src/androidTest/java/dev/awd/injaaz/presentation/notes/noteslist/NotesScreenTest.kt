package dev.awd.injaaz.presentation.notes.noteslist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.Note
import dev.awd.injaaz.ui.theme.InjaazTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity by lazy { composeTestRule.activity }
    private var tempNote = Note(0, "New Note", "Note Content", 0)
    var isDeleted = false

    @Before
    fun setup() {
        composeTestRule.setContent {
            InjaazTheme {
                NotesScreen(
                    uiState = NotesUiState.Notes(listOf(tempNote)),
                    onNoteClick = {},
                    onDeleteIconClick = { isDeleted = true })
            }
        }
    }

    @Test
    fun onLaunch_NotesShown() {
        composeTestRule.onRoot().printToLog("")
        composeTestRule.onNodeWithText(tempNote.title).assertExists()
    }

    @Test
    fun noteLongClick_DeleteIconAppears() {
        composeTestRule.onNodeWithText(tempNote.title).performTouchInput { longClick() }
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.delete))
            .assertIsDisplayed()
    }

    @Test
    fun deleteIconClick_noteIsDeleted() {
        composeTestRule.onRoot().printToLog("")
        composeTestRule.onNodeWithText(tempNote.title).performTouchInput { longClick() }
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.delete))
            .assertIsDisplayed().performClick()
        Assert.assertTrue(isDeleted)
    }
}

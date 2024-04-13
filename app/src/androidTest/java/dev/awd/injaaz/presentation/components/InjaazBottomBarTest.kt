package dev.awd.injaaz.presentation.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dev.awd.injaaz.presentation.BottomBarScreen
import dev.awd.injaaz.ui.theme.InjaazTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InjaazBottomBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private var currentScreen =
        BottomBarScreen.Tasks

    @Before
    fun setup() {
        val bottomScreens = BottomBarScreen.entries
        composeTestRule.setContent {
            InjaazTheme {
                InjaazBottomBar(
                    screens = bottomScreens,
                    currentScreen = currentScreen,
                    onTabSelected = { currentScreen = it })
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("Setup")
    }

    @Test
    fun injaazBottomBarTest_currentTabSelected() {
        findTasksTab().assertIsSelected()
    }

    @Test
    fun selectTab_currentScreenChanged() {

        findNotesTab().performClick() //Issue: Click happens before app is loaded so we have to test state not behaviour
        Assert.assertEquals(BottomBarScreen.Notes, currentScreen)

    }


    private fun findNotesTab() = composeTestRule.onNode(
        hasAnyChild(
            hasText(
                composeTestRule.activity.getString(
                    BottomBarScreen.Notes.title
                )
            )
        ) and hasClickAction(),
        useUnmergedTree = true
    )

    private fun findTasksTab() = composeTestRule.onNode(
        hasAnyChild(
            hasText(
                composeTestRule.activity.getString(
                    BottomBarScreen.Tasks.title
                )
            )
        ) and hasClickAction(),
        useUnmergedTree = true
    )

}
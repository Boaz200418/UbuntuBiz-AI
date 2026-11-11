package africa.ubuntubiz.ai

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class AppSmokeTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()
    @Test fun navigationValidationAndDirectorySearch() {
        compose.onNodeWithText("Create My Business Story").performScrollTo().performClick()
        compose.onNodeWithText("Transform My Story").performScrollTo().performClick()
        compose.onNodeWithText("Please enter your name.").assertExists()
        compose.onNodeWithText("Discover",useUnmergedTree=true).performClick()
        compose.onNodeWithText("Search businesses, skills or countries").performTextInput("Kijani")
        compose.onNodeWithText("Kijani Threads").assertExists()
        compose.onNodeWithText("Search businesses, skills or countries").performTextClearance()
        compose.onNodeWithText("Search businesses, skills or countries").performTextInput("no-such-business-123")
        compose.onNodeWithText("No businesses found").assertExists()
    }
}

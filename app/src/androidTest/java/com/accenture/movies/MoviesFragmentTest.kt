package com.accenture.movies

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Tells JUnit that this is an Android instrumentation test.
// It will run on an emulator or real device.
@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    // Starts MainActivity before each test.
    // Because MainActivity opens MoviesFragment,
    // this also opens your Fragment screen.
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun searchEditText_isDisplayed() {
        // Finds the EditText by ID and checks that it is visible.
        onView(withId(R.id.searchEditText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchEditText_hasCorrectHint() {
        // Checks that the search field has the expected hint text.
        onView(withId(R.id.searchEditText))
            .check(matches(withHint("Search items...")))
    }

    @Test
    fun userCanTypeIntoSearchEditText() {
        // Finds the search EditText.
        onView(withId(R.id.searchEditText))
            // Clears existing text if there is any.
            .perform(clearText())
            // Types "Batman" into the field.
            .perform(typeText("Batman"))
            // Closes the keyboard so it does not cover the UI.
            .perform(closeSoftKeyboard())

        // Verifies that the typed text is really inside the EditText.
        onView(withId(R.id.searchEditText))
            .check(matches(withText("Batman")))
    }

    @Test
    fun stateTextView_isDisplayed() {
        // Checks that the TextView used for Loading/Error state exists and is visible.
        onView(withId(R.id.stateTextView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun moviesRecyclerView_exists() {
        // Checks that the RecyclerView exists in the layout.
        // It may be GONE depending on Loading/Error/Success state,
        // so this test only checks that Espresso can find it.
        onView(withId(R.id.moviesRecyclerView))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }
}
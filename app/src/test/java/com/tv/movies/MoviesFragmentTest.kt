package com.tv.movies

import com.tv.movies.ui.screens.MoviesFragment
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MoviesFragmentTest {

    @Test
    fun fragmentIsCreated() {
        // Starts MainActivity in Robolectric, without emulator
        val activity = Robolectric.buildActivity(MainActivity::class.java)
            .setup()
            .get()

        // MainActivity puts MoviesFragment into android.R.id.content
        val fragment = activity.supportFragmentManager
            .findFragmentById(android.R.id.content)

        // Checks that the Fragment really exists
        assertNotNull(fragment)

        // Optional: check exact type
        assert(fragment is MoviesFragment)
    }
}
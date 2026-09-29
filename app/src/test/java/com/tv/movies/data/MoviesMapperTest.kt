package com.tv.movies.data

import com.tv.movies.data.dto.MovieDto
import com.tv.movies.data.mapper.toDomainMovie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoviesMapperTest {

    private fun dto(poster: String) =
        MovieDto(title = "Batman", year = "1989", imdbID = "1", type = "movie", poster = poster)

    @Test
    fun `poster url is kept`() {
        val url = "https://m.media-amazon.com/images/M/poster.jpg"

        assertEquals(url, dto(url).toDomainMovie().poster)
    }

    @Test
    fun `N_A poster is mapped to null`() {
        assertNull(dto("N/A").toDomainMovie().poster)
    }

    @Test
    fun `blank poster is mapped to null`() {
        assertNull(dto("  ").toDomainMovie().poster)
    }
}

package com.accenture.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.accenture.movies.ui.theme.MoviesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MainActivity : ComponentActivity() {

    private val api = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
        .build()
        .create(MoviesService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = api.searchMovies("Matrix")
                setContent {
                    MoviesTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Column(Modifier.padding(innerPadding)) {
                                response.movies?.forEach { movie ->
                                    MovieItem(movie.title)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

@Composable
fun MovieItem(name: String?, modifier: Modifier = Modifier) {
    Text(
        text = "Movie: $name",
        modifier = modifier
    )
}

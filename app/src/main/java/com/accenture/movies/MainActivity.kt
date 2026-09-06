package com.accenture.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.accenture.movies.ui.screens.MoviesScreen
import com.accenture.movies.ui.theme.MoviesTheme
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MoviesTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MoviesScreen(innerPadding = innerPadding)
//                }
//            }
//        }
//    }
//}



import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.accenture.movies.ui.screens.MoviesFragment

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, MoviesFragment())
                .commit()
        }
    }
}


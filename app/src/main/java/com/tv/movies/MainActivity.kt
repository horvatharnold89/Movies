package com.tv.movies

import android.os.Bundle
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


import androidx.fragment.app.FragmentActivity
import com.tv.movies.ui.screens.MoviesFragment

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


package com.tv.movies.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tv.movies.R
import com.tv.movies.ui.adapter.MoviesAdapter
import com.tv.movies.ui.viewmodels.MoviesViewModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val viewModel: MoviesViewModule by viewModels()
    private lateinit var adapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val stateTextView = view.findViewById<TextView>(R.id.stateTextView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.moviesRecyclerView)

        adapter = MoviesAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        searchEditText.doAfterTextChanged {
            viewModel.getMovies(it.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MoviesViewModule.UiState.Loading -> {
                            stateTextView.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            stateTextView.text = "Loading..."
                        }

                        is MoviesViewModule.UiState.Error -> {
                            stateTextView.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            stateTextView.text = state.message
                        }

                        is MoviesViewModule.UiState.Success -> {
                            stateTextView.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            adapter.submitList(state.movies)
                        }
                    }
                }
            }
        }
    }
}
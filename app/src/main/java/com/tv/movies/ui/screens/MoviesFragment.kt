package com.tv.movies.ui.screens

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
import kotlin.math.max

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val viewModel: MoviesViewModule by viewModels()
    private lateinit var adapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val stateTextView = view.findViewById<TextView>(R.id.stateTextView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.moviesRecyclerView)

        applyWindowInsets(view)

        adapter = MoviesAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard(searchEditText)
                }
            }
        })

        // Show the ViewModel's current query (e.g. the initial "Matrix") before listening,
        // so the field matches the results. Restored state after rotation overrides this.
        val currentQuery = viewModel.searchQuery.value
        searchEditText.setText(currentQuery)
        searchEditText.setSelection(currentQuery.length)

        searchEditText.doAfterTextChanged {
            viewModel.getMovies(it.toString())
        }
        searchEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(textView)
                true
            } else {
                false
            }
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

    /** Keeps content clear of the status bar, cutout, navigation bar and keyboard (edge-to-edge). */
    private fun applyWindowInsets(root: View) {
        val initialLeft = root.paddingLeft
        val initialTop = root.paddingTop
        val initialRight = root.paddingRight
        val initialBottom = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.updatePadding(
                left = initialLeft + bars.left,
                top = initialTop + bars.top,
                right = initialRight + bars.right,
                bottom = initialBottom + max(bars.bottom, ime.bottom)
            )
            insets
        }
    }

    private fun hideKeyboard(focusedView: View) {
        focusedView.clearFocus()
        WindowCompat.getInsetsController(requireActivity().window, focusedView)
            .hide(WindowInsetsCompat.Type.ime())
    }
}
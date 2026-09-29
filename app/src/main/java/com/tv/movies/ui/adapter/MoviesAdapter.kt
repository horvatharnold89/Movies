package com.tv.movies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tv.movies.R
import com.tv.movies.domain.model.Movie

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView =
            itemView.findViewById(R.id.movieTitleTextView)

        private val yearTextView: TextView =
            itemView.findViewById(R.id.movieYearTextView)

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
            yearTextView.text = movie.year
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
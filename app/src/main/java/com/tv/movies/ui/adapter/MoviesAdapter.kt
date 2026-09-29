package com.tv.movies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.dispose
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
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

    override fun onViewRecycled(holder: MovieViewHolder) {
        super.onViewRecycled(holder)
        holder.posterImageView.dispose()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val posterImageView: ImageView =
            itemView.findViewById(R.id.moviePosterImageView)

        private val titleTextView: TextView =
            itemView.findViewById(R.id.movieTitleTextView)

        private val yearTextView: TextView =
            itemView.findViewById(R.id.movieYearTextView)

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
            yearTextView.text = movie.year
            posterImageView.contentDescription = movie.title
            // A null poster goes straight to the error drawable.
            posterImageView.load(movie.poster) {
                crossfade(true)
                placeholder(R.drawable.poster_placeholder)
                error(R.drawable.poster_placeholder)
            }
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
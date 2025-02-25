package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.database.FavoriteMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteMovieAdapter(
    private val favoriteMovies: MutableList<FavoriteMovie>,
    private val database: AppDatabase
) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    class FavoriteMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moviePoster: ImageView = view.findViewById(R.id.moviePoster)
        val movieTitle: TextView = view.findViewById(R.id.movieTitle)
        val movieYear: TextView = view.findViewById(R.id.movieYear)
        val removeFavorite: ImageView = view.findViewById(R.id.removeFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourite_movie_items, parent, false)
        return FavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val movie = favoriteMovies[position]

        holder.movieTitle.text = movie.title
        holder.movieYear.text = movie.year
        Glide.with(holder.itemView.context).load(movie.posterUrl).into(holder.moviePoster)

        // Remove movie from favorites
        holder.removeFavorite.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                database.favoriteMovieDao().deleteMovie(movie)
                favoriteMovies.removeAt(position)
                launch(Dispatchers.Main) {
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = favoriteMovies.size
}

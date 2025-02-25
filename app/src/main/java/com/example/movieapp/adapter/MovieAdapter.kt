package com.example.movieapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.activities.MovieDetailActivity
import com.example.movieapp.model.Movie

class MovieAdapter(private val movieList: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.titleTextView.text = movie.Title
        holder.yearTextView.text = movie.Year
        Glide.with(holder.posterImageView.context)
            .load(movie.Poster)
            .into(holder.posterImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieDetailActivity::class.java)
            intent.putExtra("imdbID", movie.imdbID)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = movieList.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val yearTextView: TextView = itemView.findViewById(R.id.yearTextView)
        val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
    }
}

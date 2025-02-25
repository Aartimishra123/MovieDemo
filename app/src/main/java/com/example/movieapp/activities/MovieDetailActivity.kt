package com.example.movieapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.api.RetrofitClient
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.database.FavoriteMovie
import com.example.movieapp.model.MovieDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailActivity : ComponentActivity() {
    private lateinit var favoriteButton: Button
    private lateinit var database: AppDatabase
    private var isFavorite = false
    private lateinit var imdbID: String
    private lateinit var strmovieTitle: String
    private lateinit var strmovieYear: String
    private lateinit var moviePosterUrl: String
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        database = AppDatabase.getDatabase(this)
        progressBar = findViewById(R.id.progressBar)

        // Get IMDb ID from Intent
         imdbID = intent.getStringExtra("imdbID") ?: return
        fetchMovieDetails(imdbID)
        // Check if movie is already a favorite
        CoroutineScope(Dispatchers.IO).launch {
            isFavorite = database.favoriteMovieDao().isFavorite(imdbID)
            withContext(Dispatchers.Main) {
                updateFavoriteButton()
            }
        }
        favoriteButton = findViewById<Button>(R.id.favoriteButton)

        // Toggle Favorite Button
        favoriteButton.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun fetchMovieDetails(imdbID: String) {
        progressBar.visibility = View.VISIBLE  // Show loading indicator

        val apiKey = "10b57cf"
        val call = RetrofitClient.apiService.getMovieDetail(apiKey, imdbID)
        call.enqueue(object : Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                progressBar.visibility = View.GONE  // Hide loading indicator

                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        displayMovieDetails(movie)
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                progressBar.visibility = View.GONE  // Hide loading indicator
                Log.e("MovieDetailActivity", "Error: ${t.message}")
            }
        })
    }

    private fun displayMovieDetails(movie: MovieDetail) {
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val movieYear = findViewById<TextView>(R.id.movieYear)
        val movieGenre = findViewById<TextView>(R.id.movieGenre)
        val movieDirector = findViewById<TextView>(R.id.movieDirector)
        val movieActors = findViewById<TextView>(R.id.movieActors)
        val moviePlot = findViewById<TextView>(R.id.moviePlot)
        val movieRating = findViewById<TextView>(R.id.movieRating)
        val moviePoster = findViewById<ImageView>(R.id.moviePoster)

        movieTitle.text = movie.Title
        movieYear.text = "Year: ${movie.Year}"
        movieGenre.text = "Genre: ${movie.Genre}"
        movieDirector.text = "Director: ${movie.Director}"
        movieActors.text = "Actors: ${movie.Actors}"
        moviePlot.text = movie.Plot
        movieRating.text = "IMDb Rating: ${movie.imdbRating}"

        // Load image using Glide
        Glide.with(this)
            .load(movie.Poster)
            .into(moviePoster)

        strmovieTitle = movie.Title
        strmovieYear = movie.Year
        moviePosterUrl = movie.Poster
    }

    private fun toggleFavorite() {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = FavoriteMovie(imdbID, strmovieTitle, strmovieYear, moviePosterUrl)

            if (isFavorite) {
                database.favoriteMovieDao().deleteMovie(movie)
            } else {
                database.favoriteMovieDao().insertMovie(movie)
            }

            isFavorite = !isFavorite
            withContext(Dispatchers.Main) {
                updateFavoriteButton()
            }
        }
    }

    private fun updateFavoriteButton() {
        favoriteButton.text = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
    }
}

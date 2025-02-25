package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.activities.FavoriteMoviesActivity

import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.api.RetrofitClient
import com.example.movieapp.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchButton)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchMovies(query)
            } else {
                Toast.makeText(this, "Please enter a movie name", Toast.LENGTH_SHORT).show()
            }
        }
        val favoritesButton = findViewById<Button>(R.id.favoritesButton)
        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoriteMoviesActivity::class.java))
        }
    }

    private fun searchMovies(query: String) {
        progressBar.visibility = View.VISIBLE  // Show loading indicator

        val apiKey = "10b57cf"
        RetrofitClient.apiService.getMovies(apiKey, query)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    progressBar.visibility = View.GONE  // Hide loading indicator

                    if (response.isSuccessful && response.body() != null) {
                        val movies = response.body()!!.Search

                        if (movies.isNullOrEmpty()) {
                            Toast.makeText(this@MainActivity, "No movies found!", Toast.LENGTH_SHORT).show()
                        } else {
                            movieAdapter = MovieAdapter(movies)
                            recyclerView.adapter = movieAdapter
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to load movies", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE  // Hide loading indicator
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}

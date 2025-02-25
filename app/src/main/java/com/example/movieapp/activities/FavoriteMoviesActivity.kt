package com.example.movieapp.activities
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.FavoriteMovieAdapter
import com.example.movieapp.database.AppDatabase
import kotlinx.coroutines.*

class FavoriteMoviesActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var noFavoritesText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_movies)

        database = AppDatabase.getDatabase(this)
        noFavoritesText = findViewById<TextView>(R.id.noFavoritesText)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFavorites)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load favorites from Room database
        CoroutineScope(Dispatchers.IO).launch {
            val favorites = database.favoriteMovieDao().getAllFavorites().toMutableList()

            // Switch to main thread before updating UI
            withContext(Dispatchers.Main) {
                if (favorites.isEmpty()) {
                    noFavoritesText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    noFavoritesText.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter = FavoriteMovieAdapter(favorites, database)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

}

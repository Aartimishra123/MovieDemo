package com.example.movieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val imdbID: String,
    val title: String,
    val year: String,
    val posterUrl: String
)
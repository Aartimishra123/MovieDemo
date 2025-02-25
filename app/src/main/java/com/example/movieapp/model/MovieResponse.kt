package com.example.movieapp.model


data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)
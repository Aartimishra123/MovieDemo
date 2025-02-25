package com.example.movieapp.api

import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    fun getMovies(@Query("apikey") apiKey: String, @Query("s") searchQuery: String): Call<MovieResponse>


    @GET("?")
    fun getMovieDetail(@Query("apikey") apiKey: String, @Query("i") imdbID: String): Call<MovieDetail>

}
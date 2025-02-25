package com.example.movieapp.database

import androidx.room.*

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovie)

    @Delete
    suspend fun deleteMovie(movie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavorites(): List<FavoriteMovie>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE imdbID = :imdbID)")
    suspend fun isFavorite(imdbID: String): Boolean
}

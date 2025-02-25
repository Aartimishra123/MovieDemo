package com.example.movieapp

import android.content.Context

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.database.FavoriteMovie
import com.example.movieapp.database.FavoriteMovieDao

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FavoriteMovieDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: FavoriteMovieDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()  // Only for testing
            .build()

        dao = database.favoriteMovieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insertandretrievefavoritemovie`() = runBlocking {
        val movie = FavoriteMovie("tt0372784", "Batman Begins", "2005", "https://example.com/batman.jpg")
        dao.insertMovie(movie)

        val favorites = dao.getAllFavorites()
        assertEquals(1, favorites.size)
        assertEquals("Batman Begins", favorites.first().title)
    }

    @Test
    fun `removefavoritemovie`() = runBlocking {
        val movie = FavoriteMovie("tt0372784", "Batman Begins", "2005", "https://example.com/batman.jpg")
        dao.insertMovie(movie)
        dao.deleteMovie(movie)

        val favorites = dao.getAllFavorites()
        assertEquals(0, favorites.size)
    }



}

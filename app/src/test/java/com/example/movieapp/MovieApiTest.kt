package com.example.movieapp

import com.example.movieapp.api.ApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))  // Use mock server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `searchMovies should return a list of movies`() {
        val mockResponse = """
            {
                "Search": [
                    {
                        "Title": "Batman Begins",
                        "Year": "2005",
                        "imdbID": "tt0372784",
                        "Type": "movie",
                        "Poster": "https://example.com/batman.jpg"
                    }
                ],
                "totalResults": "1",
                "Response": "True"
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val response = apiService.getMovies("10b57cf", "batman").execute()

        assertEquals(true, response.isSuccessful)
        assertEquals(1, response.body()?.Search?.size)
        assertEquals("Batman Begins", response.body()?.Search?.first()?.Title)
    }

    @Test
    fun `getMovieDetail should return movie details`() {
        val mockResponse = """
            {
                "Title": "Batman Begins",
                "Year": "2005",
                "imdbID": "tt0372784",
                "Type": "movie",
                "Poster": "https://example.com/batman.jpg",
                "Plot": "Bruce Wayne becomes Batman."
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val response = apiService.getMovieDetail("10b57cf", "tt0372784").execute()

        assertEquals(true, response.isSuccessful)
        assertEquals("Batman Begins", response.body()?.Title)
    }



  //  Handle empty search query and malformed API response.
  @Test
  fun `searchMovies should return empty when no results`() {
      val mockResponse = """
        {
            "Search": [],
            "totalResults": "0",
            "Response": "True"
        }
    """.trimIndent()

      mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

      val response = apiService.getMovies("10b57cf", "unknownmovie").execute()

      assertEquals(true, response.isSuccessful)
      assertEquals(0, response.body()?.Search?.size)
  }

    @Test
    fun `searchMovies should handle malformed response`() {
        val mockResponse = "{ invalid json }" // Malformed JSON

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val response = apiService.getMovies("10b57cf", "batman").execute()

        assertEquals(false, response.isSuccessful)
    }
}

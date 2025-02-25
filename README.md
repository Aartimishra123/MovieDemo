Movie App - README

This is an Android Movie App that allows users to search for movies using the OMDb API, view movie details, and mark/unmark movies as favorites. The app follows the MVVM architecture and utilizes Room Database for local persistence.

📌 Features

Search Movies: Users can search for movies using the OMDb API.

Movie Details: Users can view detailed information about a selected movie.

Favorite Movies: Users can mark movies as favorites and persist them locally.

Offline Support: Favorite movies are stored using Room and available offline.

Error Handling: Handles API failures, empty search results, and network errors gracefully.

🛠️ Tech Stack

Programming Language: Kotlin

UI: XML

Networking: Retrofit

Image Loading: Glide

Local Database: Room (with Coroutines)

Unit Testing: JUnit, Mockito

🔌 API Integration

OMDb API

Base URL: https://www.omdbapi.com/

API Key: your api key

Endpoints Used:

GET /?apikey={apiKey}&s={searchQuery} - Fetches search results.

GET /?apikey={apiKey}&i={imdbID} - Fetches movie details.

Usage

To use the Movies App, follow these steps:
1. Open the app.
2. Enter a movie title in the search bar and press search button.
3. Wait for the search results to load.
4. Click on a movie to view its details.
5. To add a movie to your favorites, click on the Add favourites button in the movie details screen.
6. To view your favorites, click on tfavourties button

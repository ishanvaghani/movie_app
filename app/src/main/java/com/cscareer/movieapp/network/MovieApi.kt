package com.cscareer.movieapp.network

import com.cscareer.movieapp.data.model.MovieDetails
import com.cscareer.movieapp.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    companion object {
        private const val API_KEY = "1fcbeec0cdad8ddf7fe75e1a6cf41d18"
        const val PHOTO_BASE_URL = "https://image.tmdb.org/t/p/w342"
    }

    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): Response<MovieDetails>

    @GET("movie/now_playing?api_key=$API_KEY")
    suspend fun getNowPlayingMovies(): Response<MovieResponse>

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieResponse

}
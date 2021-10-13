package com.cscareer.movieapp.network

import com.cscareer.movieapp.data.model.MovieDetails
import com.cscareer.movieapp.data.model.MovieResponse
import retrofit2.Response
import javax.inject.Inject

class ApiBuilder @Inject constructor(private val movieApi: MovieApi) {

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetails> =
        movieApi.getMovieDetails(movieId)

    suspend fun getNowPlayingMovies(): Response<MovieResponse> = movieApi.getNowPlayingMovies()
}
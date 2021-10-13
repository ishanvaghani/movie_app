package com.cscareer.movieapp.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.cscareer.movieapp.data.model.Movie
import com.cscareer.movieapp.data.model.MovieDetails
import com.cscareer.movieapp.network.ApiBuilder
import com.cscareer.movieapp.network.MovieApi
import com.cscareer.movieapp.pagination.PopularMoviePagingSource
import com.cscareer.movieapp.util.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val apiBuilder: ApiBuilder,
    @ApplicationContext private val context: Context
) {

    var movieDetails: MovieDetails? = null
    var nowPlayingMovies: List<Movie>? = null

    suspend fun getMovieDetails(movieId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getMovieDetails(movieId)
            if (response.isSuccessful) {
                movieDetails = response.body()!!
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            withContext(Dispatchers.Main) {
                showToast(context)
            }
        }
    }

    suspend fun getNowPlayingMovies() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getNowPlayingMovies()
            if (response.isSuccessful) {
                nowPlayingMovies = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    fun getPopularMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PopularMoviePagingSource(movieApi) }
        ).liveData
}
package com.cscareer.movieapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cscareer.movieapp.data.model.MovieDetails
import com.cscareer.movieapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieRepository: MovieRepository):
    ViewModel() {

    private var movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()

    fun readyMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieDetails(movieId)
            movieDetails.value = movieRepository.movieDetails
        }
    }

    fun getMovieDetails(): LiveData<MovieDetails> = movieDetails
}
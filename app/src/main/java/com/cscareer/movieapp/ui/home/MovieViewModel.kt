package com.cscareer.movieapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cscareer.movieapp.data.model.Movie
import com.cscareer.movieapp.data.model.MovieDetails
import com.cscareer.movieapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private var nowPlayingMovies: MutableLiveData<List<Movie>> = MutableLiveData()

    fun readyNowPlayingMovies() {
        viewModelScope.launch {
            movieRepository.getNowPlayingMovies()
            nowPlayingMovies.value = movieRepository.nowPlayingMovies
        }
    }

    fun getNowPlayingMovies(): LiveData<List<Movie>> = nowPlayingMovies

    val popularMovies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
}
package com.leysa.themovieapplication.ui.viewmodels
import androidx.lifecycle.ViewModel
import com.leysa.themovieapplication.data.Movie
import androidx.lifecycle.viewModelScope
import com.leysa.themovieapplication.data.MovieDetails
import com.leysa.themovieapplication.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails: StateFlow<MovieDetails?> get() = _movieDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Obtener las películas en tendencia
    fun fetchTrendingMovies(apiKey: String, timeWindow: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val moviesResponse = RetrofitInstance.apiService.getTrendingMovies(timeWindow, apiKey)  // Pasar timeWindow
                _movies.value = moviesResponse.results
            } catch (e: Exception) {
                e.printStackTrace()
                _movies.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Obtener detalles de una película
    fun fetchMovieDetails(movieId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val movieDetailsResponse = RetrofitInstance.apiService.getMovieDetails(movieId, apiKey)
                _movieDetails.value = movieDetailsResponse
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

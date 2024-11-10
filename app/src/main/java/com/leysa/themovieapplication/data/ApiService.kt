package com.leysa.themovieapplication.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Endpoint para obtener películas en tendencia
    @GET("trending/movie/{timeWindow}")
    suspend fun getTrendingMovies(
        @Path("timeWindow") timeWindow: String,  // Se pasa el timeWindow como parámetro
        @Query("api_key") apiKey: String
    ): MovieListResponse
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): MovieDetails
}

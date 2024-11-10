package com.leysa.themovieapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponse(
    val results: List<Movie>
)

@Serializable
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String?
)
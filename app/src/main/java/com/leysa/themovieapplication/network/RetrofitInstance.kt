package com.leysa.themovieapplication.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.leysa.themovieapplication.data.ApiService
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val json = Json { ignoreUnknownKeys = true }

    val apiService: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}

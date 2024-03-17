package com.ru.movieshows.sources.base

import com.google.gson.Gson
import retrofit2.Retrofit

class NetworkConfig(
    val retrofit: Retrofit,
    val gson: Gson,
    val imageUrl: String,
)
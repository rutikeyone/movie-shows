package com.ru.movieshows.data.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl() = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Authorization", "Bearer $API_KEY")
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        gson: Gson,
        client: OkHttpClient
    ): Retrofit {
        val gsonConverterFactory = GsonConverterFactory.create(gson)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    companion object {
        private const val API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NzAyMTcxODRhZGUyZWRlY2VlMmI3MWY1ODQyYjM1YSIsInN1YiI6IjY0OWFlZjU0ZDM1ZGVhMDEwYjgwOWMxNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ACObrzeuDKfRwBr_c8_HxEyjikVEF-SJ2bT9wmIHjO8"
    }

}
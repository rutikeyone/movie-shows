package com.ru.movieshows.dependencies

import com.google.gson.GsonBuilder
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.dto.GenresDto
import com.ru.movieshows.data.dto.MoviesDto
import com.ru.movieshows.data.dto.TvShowDto
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
class RetrofitModule {

    @Provides
    fun provideBaseUrl() = "https://api.themoviedb.org/3/";

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        val gsonBuilder = GsonBuilder().create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
    }

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
            chain.proceed(requestBuilder.build())
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGenresDto(retrofit: Retrofit): GenresDto = retrofit.create(GenresDto::class.java)

    @Provides
    @Singleton
    fun provideMoviesDto(retrofit: Retrofit): MoviesDto = retrofit.create(MoviesDto::class.java)

    @Provides
    @Singleton
    fun provideTvShowsDto(retrofit: Retrofit): TvShowDto = retrofit.create(TvShowDto::class.java)

    @Provides
    @Singleton
    fun provideAccountDto(retrofit: Retrofit): AccountDto = retrofit.create(AccountDto::class.java)
}
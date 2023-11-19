package com.ru.movieshows.dependencies

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.dto.GenresDto
import com.ru.movieshows.data.dto.MoviesDto
import com.ru.movieshows.data.dto.TvShowDto
import com.ru.movieshows.data.dto.YoutubeDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Named(googleApiUrlDependency)
    fun provideGoogleApiUrl() = "https://www.googleapis.com/youtube/v3/"

    @Provides
    fun provideBaseUrl() = "https://api.themoviedb.org/3/";

    @Provides
    @Singleton
    fun provideKlaxon(): Gson {
        return GsonBuilder().create()
    }

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
    @Named(googleRetrofitDependency)
    fun provideGoogleRetrofit(@Named(googleApiUrlDependency) baseUrl: String, @Named(googleApiInterceptorDependency) client: OkHttpClient): Retrofit {
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
    @Named(googleApiInterceptorDependency)
    fun provideGoogleApiClient(): OkHttpClient {
        val googleApiInterceptor = Interceptor { chain ->
            var original = chain.request()
            val url = original.url().newBuilder().addQueryParameter("key", youtubeKey).build()
            original = original.newBuilder().url(url).build()
            chain.proceed(original)
        }
        return OkHttpClient.Builder()
            .addInterceptor(googleApiInterceptor)
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

    @Provides
    @Singleton
    fun provideYoutubeDto(@Named(googleRetrofitDependency) retrofit: Retrofit): YoutubeDto = retrofit.create(YoutubeDto::class.java)

    @Provides
    @Singleton
    @Named(youtubeKeyDependency)
    fun provideYoutubeKey() = youtubeKey;

    companion object {
        const val youtubeKeyDependency = "YOUTUBE_KEY_DEPENDENCY"
        const val googleApiUrlDependency = "GOOGLE_API_DEPENDENCY"
        const val googleApiInterceptorDependency = "GOOGLE_API_INTERCEPTOR_DEPENDENCY"
        const val googleRetrofitDependency = "GOOGLE_RETROFIT_DEPENDENCY"

        private const val youtubeKey = "AIzaSyBW1-fM2kcrM3C8MVebLaXGgWlObVtNoDE"
    }
}
package com.ru.movieshows.data.dto

import com.ru.movieshows.data.response.CommentThreadResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeDto {
    @GET("commentThreads")
    suspend fun getCommentsByVideo(
        @Query("part") part: String,
        @Query("videoId") videoId: String?,
        @Query("pageToken") pageToken: String? = null
    ): CommentThreadResponse
}
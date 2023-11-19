package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.model.CommentModel
import com.ru.movieshows.data.model.PageInfoModel

data class CommentThreadResponse(
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("nextPageToken")
    val nextPageToken: String?,
    @SerializedName("pageInfo")
    val pageInfo: PageInfoModel?,
    @SerializedName("items")
    val items: ArrayList<CommentModel>?,
)
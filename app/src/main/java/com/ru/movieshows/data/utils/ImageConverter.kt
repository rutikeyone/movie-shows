package com.ru.movieshows.data.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.ru.movieshows.BuildConfig
import java.lang.reflect.Type

class ImageConverter : JsonDeserializer<String?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): String? {
        if (json == null) return null
        val value = json.asString
        return BuildConfig.TMDB_IMAGE_URL + value
    }
}
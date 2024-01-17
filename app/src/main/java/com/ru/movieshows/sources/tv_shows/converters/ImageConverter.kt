package com.ru.movieshows.sources.tv_shows.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ImageConverter() : JsonDeserializer<String?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): String? {
        if (json == null) return null
        return json.asString
    }

}
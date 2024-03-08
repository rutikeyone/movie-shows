package com.ru.movieshows.data.movies.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.ru.movieshows.data.accounts.di.BaseImageUrl
import java.lang.reflect.Type
import javax.inject.Inject

class ImageJsonDeserializer: JsonDeserializer<String?> {

    @Inject
    @BaseImageUrl
    lateinit var imageUrl: String

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): String? {
        if(json == null) return null
        val value = json.asString
        return imageUrl + value
    }

}
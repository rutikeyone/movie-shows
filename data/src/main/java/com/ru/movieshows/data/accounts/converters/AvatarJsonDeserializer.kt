package com.ru.movieshows.data.accounts.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.ru.movieshows.data.accounts.di.BaseImageUrl
import java.lang.reflect.Type
import javax.inject.Inject

class AvatarJsonDeserializer: JsonDeserializer<String?> {

    @Inject
    @BaseImageUrl
    lateinit var imageUrl: String

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        if (json == null) return null
        val value = json.asJsonObject
        val tmdb = value.getAsJsonObject("tmdb") ?: return null
        val result = tmdb.get("avatar_path")
        if(result.isJsonNull) return null
        return imageUrl + result.asString
    }

}
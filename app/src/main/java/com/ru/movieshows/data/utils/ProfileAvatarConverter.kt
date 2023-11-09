package com.ru.movieshows.data.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ProfileAvatarConverter : JsonDeserializer<String?> {

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
        return result.asString
    }

}
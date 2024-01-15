package com.ru.movieshows.sources.tv_shows.converters

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
class DateConverter : JsonDeserializer<Date?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): Date? {
        if (json == null) return null
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormatter.parse(json.asString)
    }

}
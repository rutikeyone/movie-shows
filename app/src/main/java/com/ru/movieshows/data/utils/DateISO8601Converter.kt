package com.ru.movieshows.data.utils

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date

class DateISO8601Converter: JsonDeserializer<Date?> {
    @SuppressLint("SimpleDateFormat")
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date? {
        if (json == null) return null
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return formatter.parse(json.asString)
    }
}

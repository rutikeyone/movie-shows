package com.ru.ershov.data.core.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date

 class DateJsonDeserializer : JsonDeserializer<Date?> {

    @Suppress("SimpleDateFormat")
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
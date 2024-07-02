package com.ru.movieshows.data.tv_shows.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ru.movieshows.data.tv_shows.models.TvShowModel

@Entity(
    tableName = "tv_shows_search",
    indices = [
        Index(value = ["tvShowId", "name", "locale"], unique = true)
    ],
)
data class TvShowSearchRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tvShowId: String?,
    val name: String?,
    val timeInMilSeconds: Long,
    val locale: String,
) {

    constructor(model: TvShowModel, locale: String): this(
        tvShowId = model.id,
        name = model.name,
        timeInMilSeconds = System.currentTimeMillis(),
        locale = locale,
    )

}
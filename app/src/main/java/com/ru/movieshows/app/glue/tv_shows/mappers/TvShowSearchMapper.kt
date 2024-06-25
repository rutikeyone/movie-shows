package com.ru.movieshows.app.glue.tv_shows.mappers

import com.ru.movieshows.data.tv_shows.room.TvShowSearchRoomEntity
import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch
import javax.inject.Inject

class TvShowSearchMapper @Inject constructor() {

    fun toTvShowSearch(entity: TvShowSearchRoomEntity): TvShowSearch {
        return TvShowSearch(
            id = entity.id,
            tvShowId = entity.tvShowId,
            name = entity.name,
            timeInMilSeconds = entity.timeInMilSeconds,
        )
    }

}
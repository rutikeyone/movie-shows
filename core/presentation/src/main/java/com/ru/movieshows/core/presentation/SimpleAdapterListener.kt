package com.ru.movieshows.core.presentation

interface SimpleAdapterListener<T> {

    fun onClickItem(data: T)

    fun onClickPosition(position: Int) {}

}

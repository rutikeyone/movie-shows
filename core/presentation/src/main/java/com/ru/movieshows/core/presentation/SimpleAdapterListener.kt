package com.ru.movieshows.core.presentation

fun interface SimpleAdapterListener<T> {

    fun onClickItem(data: T)

    fun onClickPosition(position: Int) {}

}

package com.ru.movieshows.app.presentation.adapters

interface SimpleAdapterListener<T> {

    fun onClickItem(data: T)

    fun onClickPosition(position: Int) {}

}

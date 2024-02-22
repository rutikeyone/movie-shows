package com.ru.movieshows.core.flow

interface LazyFlowSubjectFactory {

    fun <T> create(loader: ValueLoader<T>): LazyFlowSubject<T>

}
package com.ru.movieshows.core

interface CommonUi {

    fun toast(message: String)

    suspend fun alertDialog(config: AlertDialogConfig): Boolean

}
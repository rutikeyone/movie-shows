package com.ru.movieshows.dependencies

import com.ru.movieshows.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.presentation.sideeffects.toast.ToastsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ToastModule {

    @Binds
    abstract fun bindToast(toasts: ToastsImpl): Toasts

}
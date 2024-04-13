package com.ru.movieshows.app.glue.signin.di

import com.ru.movieshows.app.glue.signin.AdapterSignInRouter
import com.ru.movieshows.signin.presentation.SignInRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface SignInRouterModule {

    @Binds
    fun bindSignInRouter(router: AdapterSignInRouter): SignInRouter

}

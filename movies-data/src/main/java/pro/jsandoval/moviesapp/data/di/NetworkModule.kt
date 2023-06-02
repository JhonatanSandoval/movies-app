package pro.jsandoval.moviesapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.jsandoval.movieapp.utils.mvvm.coroutines.BaseUrl
import pro.jsandoval.moviesapp.data.BuildConfig
import pro.jsandoval.moviesapp.data.remote.ApiServiceBuilder
import pro.jsandoval.moviesapp.data.remote.ApiServiceBuilder.Companion.buildService
import pro.jsandoval.moviesapp.data.remote.MoviesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideMoviesApi(apiServiceBuilder: ApiServiceBuilder): MoviesApi =
        apiServiceBuilder.buildService(MoviesApi::class.java)
}
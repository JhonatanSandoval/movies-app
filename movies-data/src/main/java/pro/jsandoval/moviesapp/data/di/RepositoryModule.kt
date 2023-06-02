package pro.jsandoval.moviesapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.jsandoval.moviesapp.data.repository.MoviesRepository
import pro.jsandoval.moviesapp.data.repository.MoviesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository
}
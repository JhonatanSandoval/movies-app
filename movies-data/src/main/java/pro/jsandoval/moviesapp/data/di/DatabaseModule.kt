package pro.jsandoval.moviesapp.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pro.jsandoval.moviesapp.data.local.DB_NAME
import pro.jsandoval.moviesapp.data.local.MoviesAppDatabase
import pro.jsandoval.moviesapp.data.local.dao.MoviesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoviesAppDatabase(@ApplicationContext context: Context): MoviesAppDatabase {
        return Room.databaseBuilder(context, MoviesAppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesDao(database: MoviesAppDatabase): MoviesDao = database.moviesDao()
}
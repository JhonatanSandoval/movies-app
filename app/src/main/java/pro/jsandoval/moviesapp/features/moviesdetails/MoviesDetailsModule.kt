package pro.jsandoval.moviesapp.features.moviesdetails

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import pro.jsandoval.moviesapp.navigation.MoviesDetailsNavigator

@Module
@InstallIn(ActivityComponent::class)
interface MoviesDetailsModule {

    @Binds
    @ActivityScoped
    fun bindMoviesDetailsNavigator(impl: MoviesDetailsNavigatorImpl): MoviesDetailsNavigator
}
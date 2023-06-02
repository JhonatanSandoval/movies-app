package pro.jsandoval.movieapp.utils.resolver

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StringResourceModule {

    @Binds
    @Singleton
    fun bindsStringResourceResolver(impl: StringResourceResolverImpl): StringResourceResolver
}
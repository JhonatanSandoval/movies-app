package pro.jsandoval.movieapp.utils.utils

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pro.jsandoval.movieapp.utils.ext.isNetworkAvailable
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkChecker {

    fun isNetworkAvailable(): Boolean
}

class NetworkCheckerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkChecker {

    override fun isNetworkAvailable(): Boolean {
        return context.isNetworkAvailable()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface NetworkCheckerModule {

    @Binds
    @Singleton
    fun bindsNetworkChecker(impl: NetworkCheckerImpl): NetworkChecker
}
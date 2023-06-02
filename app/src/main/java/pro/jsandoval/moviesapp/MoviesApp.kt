package pro.jsandoval.moviesapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MoviesApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        enableLoggerIfNeeded()
    }

    private fun enableLoggerIfNeeded() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }
}
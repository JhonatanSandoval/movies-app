package pro.jsandoval.movieapp.utils.mvvm.coroutines

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

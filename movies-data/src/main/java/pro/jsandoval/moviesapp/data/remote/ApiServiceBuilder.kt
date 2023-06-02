package pro.jsandoval.moviesapp.data.remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pro.jsandoval.movieapp.utils.mvvm.coroutines.BaseUrl
import pro.jsandoval.moviesapp.data.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DEFAULT_TIMEOUT_SECONDS: Int = 30

private const val HEADER_CONTENT_TYPE = "Content-Type"
private const val HEADER_ACCEPT = "Accept"
private const val CONTENT_TYPE_JSON = "application/json"
private const val HEADER_AUTHORIZATION = "Authorization"
private const val LANGUAGE_QUERY_PARAMETER = "language"
private const val LANGUAGE_QUERY_VALUE = "es-MX"

class ApiServiceBuilder @Inject constructor(
    @BaseUrl private val baseUrl: String,
) {

    private val retrofitBuilder by lazy { Retrofit.Builder().baseUrl(baseUrl) }
    private val httpClientBuilder by lazy { getDefaultHttpClient() }
    private val gson by lazy { GsonBuilder().create() }

    private fun getDefaultHttpClient(): OkHttpClient.Builder {
        val defaultHeadersInterceptor = Interceptor { chain ->
            val chainRequest = chain.request()
            val languageQueryUrl = chainRequest.url.newBuilder()
                .addQueryParameter(LANGUAGE_QUERY_PARAMETER, LANGUAGE_QUERY_VALUE)
                .build()

            val request = chain.request().newBuilder()
                .url(languageQueryUrl)
                .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
                .addHeader(HEADER_ACCEPT, CONTENT_TYPE_JSON)
                .addHeader(HEADER_AUTHORIZATION, "Bearer ${BuildConfig.TMDB_API_KEY}")
                .build()
            chain.proceed(request)
        }
        val builder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(defaultHeadersInterceptor)
            .setTimeouts(DEFAULT_TIMEOUT_SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder
    }

    private fun OkHttpClient.Builder.setTimeouts(timeOutSeconds: Int): OkHttpClient.Builder {
        connectTimeout(timeOutSeconds.toLong(), TimeUnit.SECONDS)
        readTimeout(timeOutSeconds.toLong(), TimeUnit.SECONDS)
        writeTimeout(timeOutSeconds.toLong(), TimeUnit.SECONDS)
        return this
    }

    private fun <T> buildService(serviceClass: Class<T>): T {
        return retrofitBuilder
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClientBuilder.build())
            .build()
            .create(serviceClass)
    }

    companion object {
        fun <T> ApiServiceBuilder.buildService(serviceClass: Class<T>): T = buildService(serviceClass)
    }
}
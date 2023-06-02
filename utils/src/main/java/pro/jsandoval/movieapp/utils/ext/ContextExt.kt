package pro.jsandoval.movieapp.utils.ext

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import timber.log.Timber

fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(context)
fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)

fun Activity.requireExtrasOrFinish(vararg keys: String, onSuccess: (Bundle) -> Unit) {
    keys.forEach { key ->
        if (!this.intent.hasExtra(key)) {
            Timber.e("There is no required extra data by key: $key")
            this.finish()
            return
        }
    }
    onSuccess(intent.extras!!)
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
    return if (capabilities != null) {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } else false
}
package pro.jsandoval.movieapp.utils.mvvm.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> Fragment.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(viewLifecycleOwner) {
        if (it != null) {
            onNext(it)
        }
    }
}

fun <T> FragmentActivity.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(this) {
        if (it != null) {
            onNext(it)
        }
    }
}

fun <T> Fragment.subscribeNullable(liveData: LiveData<T>, onNext: (t: T?) -> Unit) {
    liveData.observe(viewLifecycleOwner) { onNext(it) }
}

fun <T> FragmentActivity.subscribeNullable(liveData: LiveData<T>, onNext: (t: T?) -> Unit) {
    liveData.observe(this) { onNext(it) }
}

fun <T, L> L.subscribe(liveData: LiveData<T>, onNext: (t: T) -> Unit) where L : View, L : LifecycleOwner {
    liveData.observe(this) {
        if (it != null) {
            onNext(it)
        }
    }
}

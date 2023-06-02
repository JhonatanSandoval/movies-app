package pro.jsandoval.movieapp.utils.mvvm.ext

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.withBooleanLiveData(liveData: MutableLiveData<Boolean>): Flow<T> {
    return this
        .onStart { liveData.postValue(true) }
        .onCompletion { liveData.postValue(false) }
}
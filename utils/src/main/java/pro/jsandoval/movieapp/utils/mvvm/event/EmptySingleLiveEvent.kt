package pro.jsandoval.movieapp.utils.mvvm.event

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

class EmptySingleLiveEvent : SingleLiveEvent<Unit>() {

    @MainThread
    fun call() {
        super.setValue(Unit)
    }

    @WorkerThread
    fun postCall() {
        super.postValue(Unit)
    }
}

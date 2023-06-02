package pro.jsandoval.movieapp.utils.mvvm.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import pro.jsandoval.movieapp.utils.mvvm.ext.subscribeNullable

interface WithFinishEvent {
    val finish: LiveData<Unit>
}

fun FragmentActivity.subscribeFinishEvent(withFinishEvent: WithFinishEvent) {
    subscribeNullable(withFinishEvent.finish) { finish() }
}
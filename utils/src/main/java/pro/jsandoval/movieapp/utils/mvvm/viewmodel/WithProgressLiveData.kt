package pro.jsandoval.movieapp.utils.mvvm.viewmodel

import androidx.lifecycle.LiveData

interface WithProgressLiveData {
    val progressVisibility: LiveData<Boolean>
}

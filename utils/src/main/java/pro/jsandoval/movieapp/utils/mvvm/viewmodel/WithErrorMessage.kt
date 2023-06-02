package pro.jsandoval.movieapp.utils.mvvm.viewmodel

import androidx.lifecycle.LiveData

interface WithErrorMessage {
    val errorMessage: LiveData<String>
}
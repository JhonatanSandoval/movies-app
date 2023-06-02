package pro.jsandoval.movieapp.utils.ext

import android.widget.EditText

val EditText.value
    get() = text?.toString() ?: ""
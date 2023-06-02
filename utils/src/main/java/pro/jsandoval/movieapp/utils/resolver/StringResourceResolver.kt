package pro.jsandoval.movieapp.utils.resolver

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface StringResourceResolver {
    fun getString(resId: Int): String
}

class StringResourceResolverImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : StringResourceResolver {

    override fun getString(resId: Int): String = context.getString(resId)
}
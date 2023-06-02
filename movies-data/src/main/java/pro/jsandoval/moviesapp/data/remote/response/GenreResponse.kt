package pro.jsandoval.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("id") val genreId: Long?,
    @SerializedName("name") val name: String?,
)

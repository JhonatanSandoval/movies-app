package pro.jsandoval.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetMoviesResponse(
    @SerializedName("results") val results: List<MovieDetailsDataResponse>?,
)
package com.submission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CastMovieResponse(
    @SerializedName("cast")
    val cast: List<CastMovieResult>,
    @SerializedName("id")
    val id: Int
)

data class CastMovieResult(
    @SerializedName("character")
    val character: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profile_path: String
)
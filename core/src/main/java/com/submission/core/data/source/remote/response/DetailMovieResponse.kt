package com.submission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailMovieResponse(
    @SerializedName("backdrop_path")
    val backdrop_path: String,
    @SerializedName("genres")
    val genres: List<DetailMovieGenre>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val poster_path: String,
    @SerializedName("release_date")
    val release_date: String,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val vote_average: Double
)

data class DetailMovieGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
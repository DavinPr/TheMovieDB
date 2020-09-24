package com.submission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TrendingMovieResponse(
    @field:SerializedName("page")
    val page: Int,
    @field:SerializedName("results")
    val results: List<TrendingMovieResult>,
    @field:SerializedName("total_pages")
    val total_pages: Int,
    @field:SerializedName("total_results")
    val total_results: Int
)

data class TrendingMovieResult(
    @field:SerializedName("backdrop_path")
    val backdrop_path: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("popularity")
    val popularity: Double,

    @field:SerializedName("poster_path")
    val poster_path: String,

    @field:SerializedName("release_date")
    val release_date: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("vote_average")
    val vote_average: Double
)
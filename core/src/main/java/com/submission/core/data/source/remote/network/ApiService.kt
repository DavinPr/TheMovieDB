package com.submission.core.data.source.remote.network

import com.submission.core.data.source.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getListDiscoverMovie(@Query("api_key") api_key: String): DiscoverMovieResponse

    @GET("trending/movie/day")
    suspend fun getListTrendingMovie(@Query("api_key") api_key: String): TrendingMovieResponse

    @GET("search/movie")
    suspend fun getListSearchMovie(
        @Query("api_key") api_key: String,
        @Query("query") query: String
    ): SearchMovieResponse

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String
    ): DetailMovieResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getListCreditMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): CastMovieResponse
}
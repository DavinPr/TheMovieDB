package com.submission.core.domain.usecase.model

data class DetailMovie(
    var id: Int = 0,
    var title: String? = "",
    var overview: String? = "",
    var popularity: Double = 0.0,
    var runtime: Int = 0,
    var backdrop_path: String? = "",
    var vote_average: Double = 0.0,
    var poster_path: String? = "",
    var release_date: String? = "",
    var genreMovie: List<GenreMovie>? = listOf()
)

data class GenreMovie(
    var id: Int,
    var name: String
)

data class CastMovie(
    var castId: Int = 0,
    var character: String? = null,
    var name: String? = null,
    var profile_path: String? = null
)
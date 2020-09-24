package com.submission.core.domain.usecase.model

data class DiscoverMovie(
    var id: Int,
    var backdrop_path: String,
    var title: String,
    var vote_average: Double = 0.0
)
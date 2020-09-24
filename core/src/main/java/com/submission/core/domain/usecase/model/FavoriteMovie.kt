package com.submission.core.domain.usecase.model

data class FavoriteMovie(
    var id: Int,
    var title: String?,
    var poster_path: String?,
    var release_date: String?
)
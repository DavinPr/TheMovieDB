package com.submission.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "discover_movie")
data class DiscoverMovieEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "backdrop_path")
    var backdrop_path: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "vote_average")
    var vote_average: Double
)
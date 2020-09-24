package com.submission.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.submission.core.data.source.local.entity.DiscoverMovieEntity
import com.submission.core.data.source.local.entity.FavoriteMovieEntity
import com.submission.core.data.source.local.entity.TrendingMovieEntity

@Database(
    entities = [
        DiscoverMovieEntity::class,
        TrendingMovieEntity::class,
        FavoriteMovieEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FilmDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}
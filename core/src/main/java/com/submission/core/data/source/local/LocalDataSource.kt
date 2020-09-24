package com.submission.core.data.source.local

import com.submission.core.data.source.local.entity.DiscoverMovieEntity
import com.submission.core.data.source.local.entity.FavoriteMovieEntity
import com.submission.core.data.source.local.entity.TrendingMovieEntity
import com.submission.core.data.source.local.room.FilmDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val filmDao: FilmDao) {
    fun getAllDiscoverMovie(): Flow<List<DiscoverMovieEntity>> = filmDao.getAllDiscoverMovie()

    fun getAllTrendingMovie(): Flow<List<TrendingMovieEntity>> = filmDao.getAllTrendingMovie()

    fun getAllFavoriteMovie(): Flow<List<FavoriteMovieEntity>> = filmDao.getAllFavoriteMovie()

    suspend fun insertDiscoverMovie(movie: List<DiscoverMovieEntity>) =
        filmDao.insertDiscoverMovie(movie)

    suspend fun insertTrendingMovie(trendingMovie: List<TrendingMovieEntity>) =
        filmDao.insertTrendingMovie(trendingMovie)

    fun insertFavoriteMovie(favoriteMovie: FavoriteMovieEntity) =
        filmDao.insertFavoriteMovie(favoriteMovie)

    fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity) =
        filmDao.deleteFavoriteMovie(favoriteMovie)

    fun checkDataFavorited(id: Int): Flow<Boolean> = filmDao.isFavorited(id)
}
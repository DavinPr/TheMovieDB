package com.submission.core.data.source.local.room

import androidx.room.*
import com.submission.core.data.source.local.entity.DiscoverMovieEntity
import com.submission.core.data.source.local.entity.FavoriteMovieEntity
import com.submission.core.data.source.local.entity.TrendingMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Query("SELECT * FROM discover_movie")
    fun getAllDiscoverMovie(): Flow<List<DiscoverMovieEntity>>

    @Query("SELECT * FROM trending_movie")
    fun getAllTrendingMovie(): Flow<List<TrendingMovieEntity>>

    @Query("SELECT * FROM favorite_movie")
    fun getAllFavoriteMovie(): Flow<List<FavoriteMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscoverMovie(movie: List<DiscoverMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingMovie(tv: List<TrendingMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Delete
    fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite_movie WHERE id = :id)")
    fun isFavorited(id: Int): Flow<Boolean>

}
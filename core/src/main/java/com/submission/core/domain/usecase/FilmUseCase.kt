package com.submission.core.domain.usecase

import com.submission.core.data.Resource
import com.submission.core.domain.usecase.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow

interface FilmUseCase {
    fun getAllDiscoverMovie(): Flow<Resource<List<DiscoverMovie>>>

    fun getAllTrendingMovie(): Flow<Resource<List<TrendingMovie>>>

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getAllSearchMovie(query: BroadcastChannel<String>): Flow<Resource<List<SearchMovie>>>

    fun getDetailMovie(id: Int): Flow<Resource<DetailMovie>>

    fun getCastMovie(id: Int): Flow<Resource<List<CastMovie>>>

    fun getFavoriteMovie(): Flow<Resource<List<FavoriteMovie>>>

    fun insertFavoriteMovie(favorite: FavoriteMovie)

    fun deleteFavoriteMovie(favorite: FavoriteMovie)

    fun checkFavorited(id: Int): Flow<Boolean>
}
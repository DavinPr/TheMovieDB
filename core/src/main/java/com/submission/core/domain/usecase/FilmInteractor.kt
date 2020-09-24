package com.submission.core.domain.usecase

import com.submission.core.data.Resource
import com.submission.core.domain.repository.IFilmRepository
import com.submission.core.domain.usecase.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow

class FilmInteractor(private val filmRepository: IFilmRepository) : FilmUseCase {
    override fun getAllDiscoverMovie(): Flow<Resource<List<DiscoverMovie>>> =
        filmRepository.getAllDiscoverMovie()

    override fun getAllTrendingMovie(): Flow<Resource<List<TrendingMovie>>> =
        filmRepository.getAllTrendingMovie()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getAllSearchMovie(query: BroadcastChannel<String>): Flow<Resource<List<SearchMovie>>> =
        filmRepository.getAllSearchMovie(query)

    override fun getDetailMovie(id: Int): Flow<Resource<DetailMovie>> =
        filmRepository.getDetailMovie(id)

    override fun getCastMovie(id: Int): Flow<Resource<List<CastMovie>>> =
        filmRepository.getCastMovie(id)

    override fun getFavoriteMovie(): Flow<Resource<List<FavoriteMovie>>> =
        filmRepository.getFavoriteMovie()

    override fun insertFavoriteMovie(favorite: FavoriteMovie) =
        filmRepository.insertFavoriteMovie(favorite)

    override fun deleteFavoriteMovie(favorite: FavoriteMovie) =
        filmRepository.deleteFavoriteMovie(favorite)

    override fun checkFavorited(id: Int): Flow<Boolean> = filmRepository.checkFavorited(id)
}
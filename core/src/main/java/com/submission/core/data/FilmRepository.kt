package com.submission.core.data

import com.submission.core.data.source.local.LocalDataSource
import com.submission.core.data.source.remote.RemoteDataSource
import com.submission.core.data.source.remote.network.ApiResponse
import com.submission.core.data.source.remote.response.DiscoverMovieResult
import com.submission.core.data.source.remote.response.TrendingMovieResult
import com.submission.core.domain.repository.IFilmRepository
import com.submission.core.domain.usecase.model.*
import com.submission.core.utils.AppExecutors
import com.submission.core.utils.DataMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*

class FilmRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IFilmRepository {
    override fun getAllDiscoverMovie(): Flow<Resource<List<DiscoverMovie>>> =
        object :
            NetworkBoundResource<List<DiscoverMovie>, List<DiscoverMovieResult>>(appExecutors) {
            override fun loadFromDB(): Flow<List<DiscoverMovie>> =
                localDataSource.getAllDiscoverMovie().map {
                    DataMapper.mapDiscoverMovieEntitiesToDomain(it)
                }

            override fun shouldFetch(data: List<DiscoverMovie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<DiscoverMovieResult>>> =
                remoteDataSource.getAllDiscoverMovie()

            override suspend fun saveCallResult(data: List<DiscoverMovieResult>) {
                val discover = DataMapper.mapDiscoverMovieResponsesToEntities(data)
                localDataSource.insertDiscoverMovie(discover)
            }

        }.asFlow()

    override fun getAllTrendingMovie(): Flow<Resource<List<TrendingMovie>>> =
        object :
            NetworkBoundResource<List<TrendingMovie>, List<TrendingMovieResult>>(appExecutors) {
            override fun loadFromDB(): Flow<List<TrendingMovie>> =
                localDataSource.getAllTrendingMovie().map {
                    DataMapper.mapTrendingMovieEntitiesToDomain(it)
                }

            override fun shouldFetch(data: List<TrendingMovie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<TrendingMovieResult>>> =
                remoteDataSource.getAllTrendingMovie()

            override suspend fun saveCallResult(data: List<TrendingMovieResult>) {
                val trending = DataMapper.mapTrendingMovieResponsesToEntities(data)
                localDataSource.insertTrendingMovie(trending)
            }
        }.asFlow()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getAllSearchMovie(query: BroadcastChannel<String>): Flow<Resource<List<SearchMovie>>> {
        return query.asFlow()
            .debounce(300)
            .distinctUntilChanged()
            .filter { it.trim().isNotEmpty() }
            .mapLatest {
                when (val apiResponse = remoteDataSource.getAllSearchMovie(it).first()) {
                    is ApiResponse.Success -> {
                        val data = DataMapper.mapSearchMovieResponseToDomain(apiResponse.data)
                        Resource.Success(data)
                    }
                    is ApiResponse.Empty -> Resource.Success(listOf())
                    is ApiResponse.Error -> Resource.Error(apiResponse.errorMessage)
                }
            }
    }

    override fun getDetailMovie(id: Int): Flow<Resource<DetailMovie>> =
        flow {
            when (val apiResponse = remoteDataSource.getDetailMovie(id).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapDetailMovieResponsesToDomain(apiResponse.data)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> emit(Resource.Success(DetailMovie()))
                is ApiResponse.Error -> emit(Resource.Error<DetailMovie>(apiResponse.errorMessage))
            }
        }

    override fun getCastMovie(id: Int): Flow<Resource<List<CastMovie>>> =
        flow {
            when (val apiResponse = remoteDataSource.getCreditMovie(id).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapCastMovieResponseToDomain(apiResponse.data)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> emit(Resource.Success<List<CastMovie>>(listOf()))
                is ApiResponse.Error -> emit(Resource.Error<List<CastMovie>>(apiResponse.errorMessage))
            }
        }

    override fun getFavoriteMovie(): Flow<Resource<List<FavoriteMovie>>> =
        flow {
            emit(Resource.Loading())
            val data = localDataSource.getAllFavoriteMovie().map {
                DataMapper.mapMovieFavoriteEntitiesToDomain(it)
            }
            emitAll(data.map {
                Resource.Success(
                    it
                )
            })
        }

    override fun insertFavoriteMovie(favoriteMovie: FavoriteMovie) {
        val data = DataMapper.mapMovieDomainToFavoriteEntities(favoriteMovie)
        appExecutors.diskIO().execute { localDataSource.insertFavoriteMovie(data) }
    }

    override fun deleteFavoriteMovie(favoriteMovie: FavoriteMovie) {
        val data = DataMapper.mapMovieDomainToFavoriteEntities(favoriteMovie)
        appExecutors.diskIO().execute { localDataSource.deleteFavoriteMovie(data) }
    }

    override fun checkFavorited(id: Int): Flow<Boolean> =
        flow {
            emitAll(localDataSource.checkDataFavorited(id))
        }

}
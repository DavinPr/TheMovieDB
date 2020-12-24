package com.submission.core.data.source.remote

import com.submission.core.data.source.remote.network.ApiResponse
import com.submission.core.data.source.remote.network.ApiService
import com.submission.core.data.source.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class RemoteDataSource(private val apiService: ApiService) {
    private val apiKey = "6d4359e131e2493dedd72daec5c5229a"

    suspend fun getAllDiscoverMovie(): Flow<ApiResponse<List<DiscoverMovieResult>>> {
        return flow {
            try {
                val response = apiService.getListDiscoverMovie(apiKey)
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllTrendingMovie(): Flow<ApiResponse<List<TrendingMovieResult>>> {
        return flow {
            try {
                val response = apiService.getListTrendingMovie(apiKey)
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllSearchMovie(query: String): Flow<ApiResponse<List<SearchMovieResult>>> {
        return flow {
            try {
                val response = apiService.getListSearchMovie(apiKey, query)
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailMovie(movieId: Int): Flow<ApiResponse<DetailMovieResponse>> {
        return flow {
            try {
                val data = apiService.getDetailMovie(movieId.toString(), apiKey)
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCreditMovie(movieId: Int): Flow<ApiResponse<List<CastMovieResult>>> {
        return flow {
            try {
                val response = apiService.getListCreditMovie(movieId, apiKey)
                val dataArray = response.cast
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
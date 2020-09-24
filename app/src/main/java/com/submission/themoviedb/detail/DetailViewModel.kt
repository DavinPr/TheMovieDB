package com.submission.themoviedb.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.core.data.Resource
import com.submission.core.domain.usecase.FilmUseCase
import com.submission.core.domain.usecase.model.CastMovie
import com.submission.core.domain.usecase.model.DetailMovie
import com.submission.core.domain.usecase.model.FavoriteMovie

class DetailViewModel(private val filmUseCase: FilmUseCase) : ViewModel() {
    fun detailData(id: Int): LiveData<Resource<DetailMovie>> =
        filmUseCase.getDetailMovie(id).asLiveData()

    fun detailCast(id: Int): LiveData<Resource<List<CastMovie>>> =
        filmUseCase.getCastMovie(id).asLiveData()

    fun stateFavorite(id: Int): LiveData<Boolean> = filmUseCase.checkFavorited(id).asLiveData()

    fun deleteFavorite(favoriteMovie: FavoriteMovie) =
        filmUseCase.deleteFavoriteMovie(favoriteMovie)

    fun insertFavorite(favoriteMovie: FavoriteMovie) =
        filmUseCase.insertFavoriteMovie(favoriteMovie)
}
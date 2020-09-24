package com.submission.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.core.domain.usecase.FilmUseCase
import com.submission.core.domain.usecase.model.FavoriteMovie

class FavoriteViewModel(private val filmUseCase: FilmUseCase) : ViewModel() {
    val getAllFavorite = filmUseCase.getFavoriteMovie().asLiveData()
    fun deleteFavoriteMovie(favorite: FavoriteMovie) = filmUseCase.deleteFavoriteMovie(favorite)
}
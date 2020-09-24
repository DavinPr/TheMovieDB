package com.submission.themoviedb.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.core.domain.usecase.FilmUseCase

class HomeViewModel(filmUseCase: FilmUseCase) : ViewModel() {
    val movieDiscover = filmUseCase.getAllDiscoverMovie().asLiveData()
    val movieTrending = filmUseCase.getAllTrendingMovie().asLiveData()
}
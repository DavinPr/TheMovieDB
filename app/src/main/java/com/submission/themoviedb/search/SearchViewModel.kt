package com.submission.themoviedb.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.core.domain.usecase.FilmUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(filmUseCase: FilmUseCase) : ViewModel() {
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)
    val searchResult = filmUseCase.getAllSearchMovie(queryChannel).asLiveData()
}
package com.submission.themoviedb.di

import com.submission.core.domain.usecase.FilmInteractor
import com.submission.core.domain.usecase.FilmUseCase
import com.submission.themoviedb.detail.DetailViewModel
import com.submission.themoviedb.home.HomeViewModel
import com.submission.themoviedb.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<FilmUseCase> { FilmInteractor(get()) }
}

@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
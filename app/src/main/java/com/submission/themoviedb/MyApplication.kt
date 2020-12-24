package com.submission.themoviedb

import android.app.Application
import com.submission.core.di.dataBaseModule
import com.submission.core.di.networkModule
import com.submission.core.di.repositoryModule
import com.submission.themoviedb.di.useCaseModule
import com.submission.themoviedb.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MyApplication : Application() {
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        //koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    dataBaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }

        //Timber
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}
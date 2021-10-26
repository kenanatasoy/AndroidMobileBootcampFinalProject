package com.example.androidmobilebootcampfinalproject

import android.app.Application
import com.example.androidmobilebootcampfinalproject.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MVVMWeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MVVMWeatherApp)
            modules(connectionLiveDataModule, dbModule, networkModule, repositoryModule, viewModelModule)
        }

    }
}

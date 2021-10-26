package com.example.androidmobilebootcampfinalproject.di

import com.example.androidmobilebootcampfinalproject.repository.WeatherForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val repositoryModule = module {
    factory { WeatherForecastRepository(get(), get()) }
}

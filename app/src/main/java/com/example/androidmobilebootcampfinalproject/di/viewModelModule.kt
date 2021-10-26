package com.example.androidmobilebootcampfinalproject.di

import com.example.androidmobilebootcampfinalproject.ui.currentWeather.CurrentWeatherViewModel
import com.example.androidmobilebootcampfinalproject.ui.weatherDetail.WeatherDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CurrentWeatherViewModel(get()) }
    viewModel { WeatherDetailViewModel(get()) }
}

package com.example.androidmobilebootcampfinalproject.ui.currentWeather

import com.example.androidmobilebootcampfinalproject.models.CurrentResponse

data class CurrentWeatherViewStateModel(private val currentForecasts: MutableList<CurrentResponse>?) {

    fun getCurrentForecasts() = currentForecasts

}


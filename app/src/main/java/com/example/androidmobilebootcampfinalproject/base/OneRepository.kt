package com.example.androidmobilebootcampfinalproject.base

import com.example.androidmobilebootcampfinalproject.localdb.CurrentForecastDAO
import com.example.androidmobilebootcampfinalproject.network.WeatherAPI

class OneRepository(override val api: WeatherAPI?, override val database: CurrentForecastDAO?)
    : BaseRepository<WeatherAPI, CurrentForecastDAO>() {

}
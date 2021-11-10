package com.example.androidmobilebootcampfinalproject.base

import com.example.androidmobilebootcampfinalproject.localdb.CurrentForecastDAO
import com.example.androidmobilebootcampfinalproject.network.WeatherAPI

abstract class BaseRepository<API : WeatherAPI?, DB : CurrentForecastDAO?> {

    abstract val api: API?
    abstract val database: DB?

}
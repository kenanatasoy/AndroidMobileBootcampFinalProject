package com.example.androidmobilebootcampfinalproject.network

import com.example.androidmobilebootcampfinalproject.models.CurrentResponse
import com.example.androidmobilebootcampfinalproject.models.ForecastResponse
import com.example.androidmobilebootcampfinalproject.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {

    @GET("current.json")
    suspend fun getCurrentForecast(@Query("key") apikey : String, @Query("q") city: String, @Query("aqi") airQuality: String) : CurrentResponse

    @GET("forecast.json")
    suspend fun getForecastDetails(@Query("key") apikey : String, @Query("q") city: String, @Query("days") days: Int, @Query("aqi") airQuality: String, @Query("alerts") alerts: String) : ForecastResponse

    @GET("search.json")
    suspend fun getSearchResults(@Query("key") apikey : String, @Query("q") city: String) : SearchResponse

}


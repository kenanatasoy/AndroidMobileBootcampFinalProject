package com.example.androidmobilebootcampfinalproject.models

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday") val forecastDay: ArrayList<ForecastDay>
)

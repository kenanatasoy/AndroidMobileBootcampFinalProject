package com.example.androidmobilebootcampfinalproject.models

import com.google.gson.annotations.SerializedName

data class Hour(
    @SerializedName("condition") val hourCondition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val temp_c: Double,
    val temp_f: Double,
    val time: String,
    val wind_kph: Double,
)
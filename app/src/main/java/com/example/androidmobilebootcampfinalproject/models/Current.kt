package com.example.androidmobilebootcampfinalproject.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Current(
    @Embedded @SerializedName("condition") val currentCondition: Condition,
    @ColumnInfo(name = "feelslike_c") val feelslike_c: Double,
    @ColumnInfo(name = "feelslike_f") val feelslike_f: Double,
    @ColumnInfo(name = "temp_c") val temp_c: Double,
    @ColumnInfo(name = "temp_f") val temp_f: Double,
)

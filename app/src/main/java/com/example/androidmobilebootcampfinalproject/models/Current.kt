package com.example.androidmobilebootcampfinalproject.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Current(
    @Embedded @SerializedName("condition") val currentCondition: Condition,
    @ColumnInfo(name = "feelslike_c") var feelslike_c: Double,
    @ColumnInfo(name = "feelslike_f") var feelslike_f: Double,
    @ColumnInfo(name = "temp_c") var temp_c: Double,
    @ColumnInfo(name = "temp_f") var temp_f: Double,
    @ColumnInfo(name = "last_updated") var last_updated: String
)

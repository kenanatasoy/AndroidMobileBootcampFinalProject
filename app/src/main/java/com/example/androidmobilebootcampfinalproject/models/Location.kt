package com.example.androidmobilebootcampfinalproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Location(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "localtime") val localtime: String,
)

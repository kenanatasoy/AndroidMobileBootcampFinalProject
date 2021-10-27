package com.example.androidmobilebootcampfinalproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @ColumnInfo(name = "name") val name: String,
)

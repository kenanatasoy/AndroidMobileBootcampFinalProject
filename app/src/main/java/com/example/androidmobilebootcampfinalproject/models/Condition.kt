package com.example.androidmobilebootcampfinalproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Condition(
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "text") val text: String
)

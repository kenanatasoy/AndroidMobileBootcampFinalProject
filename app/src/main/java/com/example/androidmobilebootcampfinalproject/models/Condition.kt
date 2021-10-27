package com.example.androidmobilebootcampfinalproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Condition(
    @ColumnInfo(name = "icon") var icon: String,
    @ColumnInfo(name = "text") var text: String
)

package com.example.androidmobilebootcampfinalproject.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CURRENTS")
data class CurrentResponse(
    @PrimaryKey @Embedded val current: Current,
    @Embedded val location: Location
)

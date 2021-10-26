package com.example.androidmobilebootcampfinalproject.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidmobilebootcampfinalproject.models.CurrentResponse

@Database(entities = [CurrentResponse::class], version = 1)
abstract class CurrentForecastDB : RoomDatabase() {

    abstract fun currentForecastDao() : CurrentForecastDAO

}


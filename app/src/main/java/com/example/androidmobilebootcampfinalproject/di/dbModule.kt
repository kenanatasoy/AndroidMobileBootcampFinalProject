package com.example.androidmobilebootcampfinalproject.di

import androidx.room.Room
import com.example.androidmobilebootcampfinalproject.localdb.CurrentForecastDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE_NAME="DB"

var dbModule = module {

    // Room Database
    single { Room.databaseBuilder(androidContext(), CurrentForecastDB::class.java, DATABASE_NAME).build() }

    // CurrentForecastsDao
    single { get<CurrentForecastDB>().currentForecastDao() }

}

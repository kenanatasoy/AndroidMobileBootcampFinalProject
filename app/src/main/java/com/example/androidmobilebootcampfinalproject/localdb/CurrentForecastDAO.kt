package com.example.androidmobilebootcampfinalproject.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidmobilebootcampfinalproject.models.CurrentResponse

@Dao
interface CurrentForecastDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentForecast(currentResponse: CurrentResponse)

    @Query("SELECT * FROM CURRENTS")
    suspend fun fetchCurrentForecasts() : MutableList<CurrentResponse>


}


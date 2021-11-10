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

    @Query("UPDATE CURRENTS SET icon=:icon, text=:text, " +
            "feelslike_c=:feelslike_c, feelslike_f=:feelslike_f, " +
            "temp_c=:temp_c, temp_f=:temp_f, last_updated=:last_updated WHERE name=:locationName")
    suspend fun updateCurrentForecast(locationName: String, icon: String, text: String,
                                      feelslike_c: Double, feelslike_f: Double,
                                      temp_c: Double, temp_f: Double, last_updated: String)


}


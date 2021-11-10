package com.example.androidmobilebootcampfinalproject.repository

import com.example.androidmobilebootcampfinalproject.localdb.CurrentForecastDAO
import com.example.androidmobilebootcampfinalproject.models.CurrentResponse
import com.example.androidmobilebootcampfinalproject.models.ForecastResponse
import com.example.androidmobilebootcampfinalproject.models.SearchResponse
import com.example.androidmobilebootcampfinalproject.network.WeatherAPI
import com.example.androidmobilebootcampfinalproject.utils.API_KEY
import com.example.androidmobilebootcampfinalproject.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single


class WeatherForecastRepository(private val api: WeatherAPI, private val currentForecastDAO: CurrentForecastDAO) {


    fun fetchSearchResultsFromRemote(text: String) = flow<Result<SearchResponse>> {

        val searchResponse = api.getSearchResults(API_KEY, text)
        emit(Result.Success(searchResponse))

    }.catch { emit(Result.Error) }
        .flowOn(Dispatchers.IO)



    fun fetchCurrentForecastFromRemote(city: String, airQuality: String) = flow<Result<CurrentResponse>> {

        val currentResponse = api.getCurrentForecast(API_KEY, city, airQuality)
        insertCurrentForecast(currentResponse).single()
        emit(Result.Success(currentResponse))

    }.catch { emit(Result.Error) }
        .flowOn(Dispatchers.IO)



    fun fetchCurrentForecastsFromDatabase() = flow<Result<MutableList<CurrentResponse>>> {

        val currentResponseListInDb = currentForecastDAO.fetchCurrentForecasts()
        emit(Result.Success(currentResponseListInDb))

    }.catch { emit(Result.Error) }
        .flowOn(Dispatchers.IO)




    fun fetchForecastDetailsFromRemote(city: String, days:Int, airQuality: String, alert: String) = flow<Result<ForecastResponse>> {

        val forecastResponse = api.getForecastDetails(API_KEY, city, days, airQuality, alert)
        emit(Result.Success(forecastResponse))


    }.catch { emit(Result.Error) }
        .flowOn(Dispatchers.IO)






    fun insertCurrentForecast(currentResponse: CurrentResponse) = flow<Result<Any>> {

        currentForecastDAO.insertCurrentForecast(currentResponse)
        emit(Result.Success(Unit))

    }.flowOn(Dispatchers.IO)




    fun updateCurrentForecast(currentResponse: CurrentResponse) = flow<Result<Any>> {

        currentForecastDAO.updateCurrentForecast(currentResponse.location.name, currentResponse.current.currentCondition.icon,
            currentResponse.current.currentCondition.text, currentResponse.current.feelslike_c,
            currentResponse.current.feelslike_f, currentResponse.current.temp_c, currentResponse.current.temp_f,
            currentResponse.current.last_updated)
        emit(Result.Success(Unit))

    }.flowOn(Dispatchers.IO)




}


package com.example.androidmobilebootcampfinalproject.ui.currentWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmobilebootcampfinalproject.models.CurrentResponse
import com.example.androidmobilebootcampfinalproject.models.SearchResponse
import com.example.androidmobilebootcampfinalproject.repository.WeatherForecastRepository
import com.example.androidmobilebootcampfinalproject.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CurrentWeatherViewModel(private val weatherForecastRepository: WeatherForecastRepository) :
    ViewModel() {


    private val _onCurrentForecastsFetchedFromDB = MutableLiveData<CurrentWeatherViewStateModel>()
    val onCurrentForecastsFetchedFromDB: LiveData<CurrentWeatherViewStateModel> = _onCurrentForecastsFetchedFromDB

    private val _onCurrentForecastsFromDBError = MutableLiveData<Unit>()
    val onCurrentForecastsFromDBError: LiveData<Unit> = _onCurrentForecastsFromDBError


    private val _onCurrentForecastFetchedFromRemote = MutableLiveData<CurrentResponse>()
    val onCurrentForecastFetchedFromRemote: LiveData<CurrentResponse> = _onCurrentForecastFetchedFromRemote

    private val _onCurrentForecastFromRemoteError = MutableLiveData<Unit>()
    val onCurrentForecastFromRemoteError: LiveData<Unit> = _onCurrentForecastFromRemoteError


    private val _onSearchResultsFetched = MutableLiveData<SearchResultsViewStateModel>()
    val onSearchResultsFetched: LiveData<SearchResultsViewStateModel> = _onSearchResultsFetched

    private val _onSearchResultsError = MutableLiveData<Unit>()
    val onSearchResultsError: LiveData<Unit> = _onSearchResultsError


    fun prepareCurrentForecastsFromDB() = viewModelScope.launch {

        weatherForecastRepository.fetchCurrentForecastsFromDatabase()
            .collect {
                when (it) {
                    is Result.Success -> {
                        _onCurrentForecastsFetchedFromDB.value =
                            CurrentWeatherViewStateModel(it.data)
                    }
                    Result.Error -> {
                        _onCurrentForecastsFromDBError.value = Unit
                    }
                }
            }
    }


    fun prepareCurrentForecastFromRemote(location: String) = viewModelScope.launch {

        weatherForecastRepository.fetchCurrentForecastFromRemote(location, "no")
            .collect {
                when (it) {

                    is Result.Success -> _onCurrentForecastFetchedFromRemote.value = it.data!!

                    Result.Error -> _onCurrentForecastFromRemoteError.value = Unit
                }
            }
    }


    fun prepareSearchResults(text: String) = viewModelScope.launch {

        weatherForecastRepository
            .fetchSearchResultsFromRemote(text)
            .collect {
                when (it) {
                    is Result.Success<SearchResponse> -> {
                        _onSearchResultsFetched.value =
                            SearchResultsViewStateModel(it.data)
                    }
                    Result.Error -> {
                        _onSearchResultsError.value = Unit
                    }
                }
            }
    }



    fun updateCurrentForecastsInDB(): MutableList<CurrentResponse> {

        // first of all, we define an empty current forecasts list(which will later
        // be filled with updated current forecasts and returned later in the code)
        // outside the runBlocking block to be able to return it,
        // return command inside runBlocking block is not valid
        var updatedCurrentResponses: MutableList<CurrentResponse> = mutableListOf()

        runBlocking(Dispatchers.Default) {

            // then here, what we're simply doing is creating a empty current forecasts list
            // and filling it with the current forecasts that are stored in the room database
            var currentResponsesInDbToUpdate: MutableList<CurrentResponse> = mutableListOf()
            weatherForecastRepository.fetchCurrentForecastsFromDatabase()
                .collect {
                    when (it) {
                        is Result.Success -> {
                            it.data.forEach { currentResponseInDb ->
                                currentResponsesInDbToUpdate.add(currentResponseInDb)
                            }
                        }
                    }
                }


            // after filling it with the data from the local DB,
            // we're iterating over every item of the list,
            // to be able to update them with the most current information
            currentResponsesInDbToUpdate.forEach { currentResponseInDb ->

                // here, we're giving every item's location name
                // to the method in the repository that'll get
                // the most current forecast information of each location
                weatherForecastRepository.fetchCurrentForecastFromRemote(currentResponseInDb.location.name,"no")
                    .collect { currentResponseFromRemote ->
                        when (currentResponseFromRemote) {
                            is Result.Success -> {

                                // if the one's name(which is in the local database) matches the one's name
                                // that's coming from the api, then we're updating the one's info in the database
                                // with the most current info of the one that's coming from the api
                                if (currentResponseInDb.location.name == currentResponseFromRemote.data.location.name) {
                                    currentResponseInDb.current.currentCondition.icon = currentResponseFromRemote.data.current.currentCondition.icon
                                    currentResponseInDb.current.currentCondition.text = currentResponseFromRemote.data.current.currentCondition.text
                                    currentResponseInDb.current.feelslike_c = currentResponseFromRemote.data.current.feelslike_c
                                    currentResponseInDb.current.feelslike_f = currentResponseFromRemote.data.current.feelslike_f
                                    currentResponseInDb.current.temp_c = currentResponseFromRemote.data.current.temp_c
                                    currentResponseInDb.current.temp_f = currentResponseFromRemote.data.current.temp_f
                                    currentResponseInDb.current.last_updated = currentResponseFromRemote.data.current.last_updated
                                }

                                // and here we're filling the list(that we defined outside
                                // the runBlocking block) with the most current forecasts
                                // that just came from the api
                                updatedCurrentResponses.add(currentResponseFromRemote.data)
                            }
                        }
                    }

                // and here we're inserting each current forecast (which we fetched
                // from the local DB then updated) to back in the database,
                // since their primary key(location name) hasn't changed,
                // they're basically the same entries of the local DB
                // with the most current available weather forecasts
                // on this conflict, the room database will only update existent entries' info
                // and not treat them as completely new entries therefore it'll not create new entries
                weatherForecastRepository.insertCurrentForecast(currentResponseInDb).single()
            }


        }

        // and here outside the runBlocking block,
        // we're just returning the most up-to-date current forecasts list
        return updatedCurrentResponses

    }



}


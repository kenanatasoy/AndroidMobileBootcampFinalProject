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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(private val weatherForecastRepository: WeatherForecastRepository): ViewModel() {

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
                when(it){
                    is Result.Success -> {
                        _onCurrentForecastsFetchedFromDB.value = CurrentWeatherViewStateModel(it.data)
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
                when(it){

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



}


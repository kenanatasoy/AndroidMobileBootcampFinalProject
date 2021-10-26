package com.example.androidmobilebootcampfinalproject.ui.weatherDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmobilebootcampfinalproject.models.ForecastResponse
import com.example.androidmobilebootcampfinalproject.repository.WeatherForecastRepository
import com.example.androidmobilebootcampfinalproject.utils.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WeatherDetailViewModel(private val weatherForecastRepository: WeatherForecastRepository): ViewModel() {

    private val _onWeatherDetailsFetched = MutableLiveData<WeatherDetailViewStateModel>()
    val onWeatherDetailsFetched: LiveData<WeatherDetailViewStateModel> = _onWeatherDetailsFetched

    private val _onWeatherDetailsError = MutableLiveData<Unit>()
    val onWeatherDetailsError: LiveData<Unit> = _onWeatherDetailsError

    private val _onWeatherDetailsLoading = MutableLiveData<Unit>()
    val onWeatherDetailsLoading: LiveData<Unit> = _onWeatherDetailsLoading


    fun prepareWeatherDetails(location: String) = viewModelScope.launch {

            weatherForecastRepository
                .fetchForecastDetailsFromRemote(location, 1, "no", "no")
                .onStart {
                    emit(Result.Loading)
                }
                .collect {
                    when (it){

                        Result.Loading -> {
                            _onWeatherDetailsLoading.value = Unit
                        }

                        is Result.Success<ForecastResponse> -> {
                            _onWeatherDetailsFetched.value = WeatherDetailViewStateModel(it.data)
                        }

                        Result.Error -> {
                            _onWeatherDetailsError.value = Unit
                        }

                    }
                }
    }


}


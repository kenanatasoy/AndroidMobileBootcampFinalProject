package com.example.androidmobilebootcampfinalproject.ui.weatherDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.androidmobilebootcampfinalproject.CoroutineTestRule
import com.example.androidmobilebootcampfinalproject.models.*
import com.example.androidmobilebootcampfinalproject.repository.WeatherForecastRepository
import com.example.androidmobilebootcampfinalproject.ui.currentWeather.SearchResultsViewStateModel
import com.example.androidmobilebootcampfinalproject.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var forecastResponse: ForecastResponse

    private val mockedWeatherDetailsSuccessfulObserver = mock<Observer<WeatherDetailViewStateModel>>()
    private val mockedWeatherDetailsErrorObserver = mock<Observer<Unit>>()
    private val mockedWeatherDetailsLoadingObserver = mock<Observer<Unit>>()

    private val mockedWeatherForecastRepository = mock<WeatherForecastRepository>()
    private lateinit var weatherDetailViewModel: WeatherDetailViewModel


    @Before
    fun setUp() {
        weatherDetailViewModel = WeatherDetailViewModel(mockedWeatherForecastRepository).apply {
            onWeatherDetailsFetched.observeForever(mockedWeatherDetailsSuccessfulObserver)
            onWeatherDetailsError.observeForever(mockedWeatherDetailsErrorObserver)
            onWeatherDetailsLoading.observeForever(mockedWeatherDetailsLoadingObserver)
        }
    }

    @Test
    fun prepareWeatherDetails_Should_Collect_Result_Success() = coroutineTestRule.runBlockingTest {

        //given
        val testLocationName = "Hamra"
        val days = 1
        val airQuality = "no"
        val alert = "no"

        val result = Result.Success(forecastResponse)
        val channel = Channel<Result<ForecastResponse>>()
        val flow = channel.consumeAsFlow()


        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchForecastDetailsFromRemote(testLocationName, days, airQuality, alert)

        launch {
            channel.send(result)
        }
        weatherDetailViewModel.prepareWeatherDetails(testLocationName)


        //then
        verify(mockedWeatherDetailsSuccessfulObserver).onChanged(
            WeatherDetailViewStateModel(forecastResponse)
        )

    }

    @Test
    fun prepareWeatherDetails_Should_Collect_Result_Error() = coroutineTestRule.runBlockingTest {

        //given
        val testLocationName = "Hamra"
        val days = 1
        val airQuality = "no"
        val alert = "no"

        val result = Result.Error
        val channel = Channel<Result.Error>()
        val flow = channel.consumeAsFlow()


        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchForecastDetailsFromRemote(testLocationName, days, airQuality, alert)

        launch {
            channel.send(result)
        }
        weatherDetailViewModel.prepareWeatherDetails(testLocationName)


        //then
        verify(mockedWeatherDetailsErrorObserver).onChanged(Unit)

    }


    @Test
    fun prepareWeatherDetails_Should_Collect_Result_Loading() = runBlocking {

        //given
        val testLocationName = "Hamra"
        val days = 1
        val airQuality = "no"
        val alert = "no"

        val result = Result.Loading
        val channel = Channel<Result<Result.Loading>>()
        val flow = channel.consumeAsFlow()


        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchForecastDetailsFromRemote(testLocationName, days, airQuality, alert)

        launch {
            channel.send(result)
        }
        weatherDetailViewModel.prepareWeatherDetails(testLocationName)


        //then
        verify(mockedWeatherDetailsLoadingObserver).onChanged(Unit)

    }



}
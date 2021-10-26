package com.example.androidmobilebootcampfinalproject.ui.currentWeather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.androidmobilebootcampfinalproject.CoroutineTestRule
import com.example.androidmobilebootcampfinalproject.models.*
import com.example.androidmobilebootcampfinalproject.repository.WeatherForecastRepository
import com.example.androidmobilebootcampfinalproject.utils.Result
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrentWeatherViewModelTest {

    @get:Rule
    var instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var currentResponse: CurrentResponse
    @Mock
    private lateinit var currentResponse2: CurrentResponse
    @Mock
    private lateinit var searchResponse: SearchResponse

    private val mockedSearchResultsSuccessfulObserver = mock<Observer<SearchResultsViewStateModel>>()
    private val mockedSearchResultsErrorObserver = mock<Observer<Unit>>()

    private val mockedCurrentForecastsFromDBObserver = mock<Observer<CurrentWeatherViewStateModel>>()
    private val mockedCurrentForecastsFromDBErrorObserver = mock<Observer<Unit>>()

    private val mockedCurrentForecastFromRemoteObserver = mock<Observer<CurrentResponse>>()
    private val mockedCurrentForecastFromRemoteErrorObserver = mock<Observer<Unit>>()


    private val mockedWeatherForecastRepository = mock<WeatherForecastRepository>()
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel


    @Before
    fun setUp() {
        currentWeatherViewModel = CurrentWeatherViewModel(mockedWeatherForecastRepository).apply {

            onSearchResultsFetched.observeForever(mockedSearchResultsSuccessfulObserver)
            onSearchResultsError.observeForever(mockedSearchResultsErrorObserver)

            onCurrentForecastsFetchedFromDB.observeForever(mockedCurrentForecastsFromDBObserver)
            onCurrentForecastsFromDBError.observeForever(mockedCurrentForecastsFromDBErrorObserver)

            onCurrentForecastFetchedFromRemote.observeForever(mockedCurrentForecastFromRemoteObserver)
            onCurrentForecastFromRemoteError.observeForever(mockedCurrentForecastFromRemoteErrorObserver)

        }
    }

    @Test
    fun prepareSearchResults_Should_Collect_ResultSuccess() = coroutineTestRule.runBlockingTest {

        //given
        val testLocationName = ""

        val result = Result.Success(searchResponse)
        val channel = Channel<Result<SearchResponse>>()
        val flow = channel.consumeAsFlow()

        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchSearchResultsFromRemote(testLocationName)

        launch {
            channel.send(result)
        }
        currentWeatherViewModel.prepareSearchResults(testLocationName)


        //then
        verify(mockedSearchResultsSuccessfulObserver).onChanged(
            SearchResultsViewStateModel(searchResponse)
        )

    }


    @Test
    fun prepareSearchResults_Should_Collect_ResultError() = coroutineTestRule.runBlockingTest {

        //given
        val testLocationName = "Ankara"

        val result = Result.Error
        val channel = Channel<Result.Error>()
        val flow = channel.consumeAsFlow()

        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchSearchResultsFromRemote(testLocationName)

        launch {
            channel.send(result)
        }
        currentWeatherViewModel.prepareSearchResults(testLocationName)

        //then
        verify(mockedSearchResultsErrorObserver).onChanged(Unit)


    }


    @Test
    fun prepareCurrentForecastsFromDB_Should_Collect_ResultSuccess() = coroutineTestRule.runBlockingTest{

        //given
        val fakeCurrentForecastMutableList: MutableList<CurrentResponse> = mutableListOf(currentResponse, currentResponse2)

        val result = Result.Success(fakeCurrentForecastMutableList)
        val channel = Channel<Result<MutableList<CurrentResponse>>>()
        val flow = channel.consumeAsFlow()

        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchCurrentForecastsFromDatabase()

        launch {
            channel.send(result)
        }
        currentWeatherViewModel.prepareCurrentForecastsFromDB()


        //then
        verify(mockedCurrentForecastsFromDBObserver).onChanged(
            CurrentWeatherViewStateModel(fakeCurrentForecastMutableList)
        )

    }


    @Test
    fun prepareCurrentForecastsFromDB_Should_Collect_ResultError() = coroutineTestRule.runBlockingTest{

        //given
        val result = Result.Error
        val channel = Channel<Result.Error>()
        val flow = channel.consumeAsFlow()

        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchCurrentForecastsFromDatabase()

        launch {
            channel.send(result)
        }
        currentWeatherViewModel.prepareCurrentForecastsFromDB()


        //then
        verify(mockedCurrentForecastsFromDBErrorObserver).onChanged(Unit)

    }


    @Test
    fun prepareCurrentForecastFromRemote_Should_Collect_ResultSuccess() = coroutineTestRule.runBlockingTest{

        //given
        val locationName = "Kamara"
        val airQuality = "no"

        val result = Result.Success(currentResponse)
        val channel = Channel<Result<CurrentResponse>>()
        val flow = channel.consumeAsFlow()

        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchCurrentForecastFromRemote(locationName, airQuality)

        launch {
            channel.send(result)
        }
        currentWeatherViewModel.prepareCurrentForecastFromRemote(locationName)


        //then
        verify(mockedCurrentForecastFromRemoteObserver).onChanged(currentResponse)

    }


    @Test
    fun prepareCurrentForecastFromRemote_Should_Collect_ResultError() = coroutineTestRule.runBlockingTest{

        //given
        val locationName = "Kamara"
        val airQuality = "no"

        val result = Result.Error
        val channel = Channel<Result.Error>()
        val flow = channel.consumeAsFlow()

        //when
        doReturn(flow)
            .whenever(mockedWeatherForecastRepository)
            .fetchCurrentForecastFromRemote(locationName, airQuality)

        launch {
            channel.send(result)
        }
        currentWeatherViewModel.prepareCurrentForecastFromRemote(locationName)

        //then
        verify(mockedCurrentForecastFromRemoteErrorObserver).onChanged(Unit)

    }







}
package com.example.androidmobilebootcampfinalproject.repository

import com.example.androidmobilebootcampfinalproject.localdb.CurrentForecastDAO
import com.example.androidmobilebootcampfinalproject.models.SearchResponse
import com.example.androidmobilebootcampfinalproject.network.WeatherAPI
import com.example.androidmobilebootcampfinalproject.utils.Result
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

private const val FAKE_API_KEY = "blablablablabla"

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherForecastRepositoryTest {

    @Mock
    private lateinit var mockedWeatherAPI: WeatherAPI

    @Mock
    private lateinit var mockedCurrentForecastDAO: CurrentForecastDAO

    @Mock
    private lateinit var searchResponse: SearchResponse


    @Test
    fun fetchSearchResultsFromRemoteFlow_ShouldReturnResultErrorOnException_collectIndexed() = runBlocking {

            // given
            val text = "Ankara"
            `when`(mockedWeatherAPI.getSearchResults(FAKE_API_KEY, text))
                .thenThrow(RuntimeException())
            val weatherForecastRepository =
                WeatherForecastRepository(mockedWeatherAPI, mockedCurrentForecastDAO)


            // when
            val fetchSearchResultsFromRemoteFlow =
                weatherForecastRepository.fetchSearchResultsFromRemote(text)


            // then
            fetchSearchResultsFromRemoteFlow.collectIndexed { index, value ->
                when (index) {
                    1 -> assert(value == Result.Error)
                }
            }

        }


    @Test
    fun fetchSearchResultsFromRemoteFlow_ShouldReturnResultSuccess_collectIndexed() = runBlocking {

        // given
        val text = "Ankara"
        `when`(mockedWeatherAPI.getSearchResults(FAKE_API_KEY, text))
            .thenReturn(searchResponse)
        val weatherForecastRepository =
            WeatherForecastRepository(mockedWeatherAPI, mockedCurrentForecastDAO)


        // when
        val fetchSearchResultsFromRemoteFlow =
            weatherForecastRepository.fetchSearchResultsFromRemote(text)


        // then
        fetchSearchResultsFromRemoteFlow.collectIndexed { index, value ->
            when (index) {
                0 -> assert(value is Result.Success)
            }
        }

    }




    //the below are the alternative tests for fetchSearchResultsFromRemote function at repository

    @Test
    fun `fetchSearchResultsFromRemoteFlow emits ResultSuccess-alternative-`()  = runBlocking {

        //given
        val testLocationName = "Ankara"

        mockedWeatherAPI.stub {
            onBlocking { getSearchResults(FAKE_API_KEY, testLocationName) } doReturn searchResponse
        }


        //when
        val weatherForecastRepository = WeatherForecastRepository(mockedWeatherAPI, mockedCurrentForecastDAO)
        val flow = weatherForecastRepository
            .fetchSearchResultsFromRemote(testLocationName)


        //then
        flow.collect { result: Result<SearchResponse> ->
            assert(result is Result.Success)
        }

    }



    @Test
    fun `fetchSearchResultsFromRemoteFlow emits ResultError-alternative-`()  = runBlocking {

        //given
        val testLocationName = "Ankara"

        mockedWeatherAPI.stub {
            onBlocking { getSearchResults(FAKE_API_KEY, testLocationName) } doAnswer {
                throw Exception()
            }
        }


        //when
        val weatherForecastRepository = WeatherForecastRepository(mockedWeatherAPI, mockedCurrentForecastDAO)
        val flow = weatherForecastRepository
            .fetchSearchResultsFromRemote(testLocationName)


        //then
        flow.collect { result: Result<SearchResponse> ->
            assert(result == Result.Error)
        }

    }



}


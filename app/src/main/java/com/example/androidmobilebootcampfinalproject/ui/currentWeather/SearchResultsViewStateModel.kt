package com.example.androidmobilebootcampfinalproject.ui.currentWeather

import com.example.androidmobilebootcampfinalproject.models.SearchResponse

data class SearchResultsViewStateModel(private val searchResponse: SearchResponse) {

        fun getSearchResults(): ArrayList<String>{

            val searchLocations: ArrayList<String> = ArrayList()

            searchResponse.forEach { searchLocation ->
              searchLocations.add(searchLocation.name)
            }

            return searchLocations
        }

}
package com.example.androidmobilebootcampfinalproject.ui.currentWeather

import android.os.Build
import android.os.Bundle
import android.util.Log
import java.time.LocalDate
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidmobilebootcampfinalproject.R
import com.example.androidmobilebootcampfinalproject.adapters.ViewPagerAdapter
import com.example.androidmobilebootcampfinalproject.adapters.OnCurrentWeatherDetailsClickHandler
import com.example.androidmobilebootcampfinalproject.base.BaseFragment
import com.example.androidmobilebootcampfinalproject.databinding.FragmentCurrentWeatherBinding
import com.example.androidmobilebootcampfinalproject.models.CurrentResponse
import com.example.androidmobilebootcampfinalproject.ui.weatherDetail.WeatherDetailFragment
import com.example.androidmobilebootcampfinalproject.utils.SemicolonTokenizer
import com.example.androidmobilebootcampfinalproject.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class CurrentWeatherFragment :
    BaseFragment<CurrentWeatherViewModel, FragmentCurrentWeatherBinding>(),
    OnCurrentWeatherDetailsClickHandler {

    private var locationNames = ArrayList<String>()

    lateinit var mactvAdapter: ArrayAdapter<String>

    lateinit var currentForecasts: MutableList<CurrentResponse>

    lateinit var viewPagerAdapter: ViewPagerAdapter

    override val viewModel: CurrentWeatherViewModel by viewModel()

    override fun getLayoutID() = R.layout.fragment_current_weather

    override fun prepareView() {


        // initial setup of the multiautocompletetextview adapter
        mactvAdapter = ArrayAdapter(
            requireContext(),
            R.layout.mactv_list_item, R.id.location, locationNames
        )

        dataBinding.multiAutoCompleteTextView.setAdapter(mactvAdapter)
        dataBinding.multiAutoCompleteTextView.setTokenizer(SemicolonTokenizer())

        // initial setup of the current forecasts and observing
        viewModel.prepareCurrentForecastsFromDB()


        dataBinding.multiAutoCompleteTextView.doAfterTextChanged { typedText ->

            val searchedText = typedText.toString().replaceBeforeLast(";", "").removePrefix(";").trim();

            if (searchedText.isNotEmpty()){

                viewModel.prepareSearchResults(searchedText)

            }
        }



        dataBinding.multiAutoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                Log.d("clickedLocation", parent!!.getItemAtPosition(position).toString())
                val clickedLocation: String = parent.getItemAtPosition(position).toString()

                locationNames = ArrayList()
                mactvAdapter.notifyDataSetChanged()


                viewModel.prepareCurrentForecastFromRemote(clickedLocation)

            }



    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeLiveData() {


        val searchToastMessage = "Please connect to the internet, otherwise no search results will appear"
        val searchToast = Toast.makeText(requireContext(), searchToastMessage, Toast.LENGTH_SHORT)

        viewModel.onSearchResultsFetched.observe(viewLifecycleOwner, {

            searchToast.cancel()

            dataBinding.searchResultsViewStateModel = it
            dataBinding.executePendingBindings()

            locationNames = dataBinding.searchResultsViewStateModel!!.getSearchResults()

            mactvAdapter = ArrayAdapter(
                requireContext(),
                R.layout.mactv_list_item, R.id.location, locationNames
            )

            dataBinding.multiAutoCompleteTextView.setAdapter(mactvAdapter)
            dataBinding.multiAutoCompleteTextView.setTokenizer(SemicolonTokenizer())

            mactvAdapter.notifyDataSetChanged()

        })

        viewModel.onSearchResultsError.observe(viewLifecycleOwner, {

            searchToast.show()

            locationNames = ArrayList()
            mactvAdapter = ArrayAdapter(
                requireContext(),
                R.layout.mactv_list_item, R.id.location, locationNames
            )
            dataBinding.multiAutoCompleteTextView.setAdapter(mactvAdapter)
            dataBinding.multiAutoCompleteTextView.setTokenizer(SemicolonTokenizer())
            mactvAdapter.notifyDataSetChanged()

        })





        viewModel.onCurrentForecastsFetchedFromDB.observe(viewLifecycleOwner, {

            dataBinding.currentWeatherViewStateModel = it
            dataBinding.executePendingBindings()

            currentForecasts = dataBinding.currentWeatherViewStateModel!!.getCurrentForecasts()!!

            val loadingToast = Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)

            if(currentForecasts.size != 0){
                val currentDate = LocalDate.now().toString()

                currentForecasts.forEach { currentForecast ->
                      val dbForecastDate = currentForecast.current.last_updated.dropLast(6)
                      if (currentDate != dbForecastDate) {
                          loadingToast.show()
                          currentForecasts = viewModel.updateCurrentForecastsInDB()
                      }
                }
            }

            viewPagerAdapter = ViewPagerAdapter(currentForecasts, this@CurrentWeatherFragment)
            dataBinding.viewPager2.adapter = viewPagerAdapter
            viewPagerAdapter.notifyDataSetChanged()
            loadingToast.cancel()

        })

        viewModel.onCurrentForecastsFromDBError.observe(viewLifecycleOwner, {
            val toastMessage = "Couldn't fetch the saved location's forecasts in the local DB"
            showToast(toastMessage, Toast.LENGTH_SHORT)
        })






        viewModel.onCurrentForecastFetchedFromRemote.observe(viewLifecycleOwner, {

            currentForecasts.add(it)
            viewPagerAdapter.notifyItemInserted(currentForecasts.size)
            dataBinding.viewPager2.setCurrentItem(currentForecasts.size - 1, true)

        })

        viewModel.onCurrentForecastFromRemoteError.observe(viewLifecycleOwner, {
            val toastMessage = "Looks like there is an issue with the internet connection, please make sure of internet connectivity"
            showToast(toastMessage, Toast.LENGTH_SHORT)
        })



    }





    override fun onCurrentWeatherDetailsClicked(position: Int) {

        val clickedItem = currentForecasts[position]

        val bundle = Bundle()
        bundle.putString("location", clickedItem.location.name)
        val weatherDetailFragment = WeatherDetailFragment()
        weatherDetailFragment.arguments = bundle

        findNavController().navigate(
            R.id.action_currentWeatherFragment_to_weatherDetailFragment,
            bundle
        )

    }


}
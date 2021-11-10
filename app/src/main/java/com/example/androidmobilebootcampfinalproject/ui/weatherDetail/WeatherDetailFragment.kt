package com.example.androidmobilebootcampfinalproject.ui.weatherDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobilebootcampfinalproject.R
import com.example.androidmobilebootcampfinalproject.adapters.HourForecastAdapter
import com.example.androidmobilebootcampfinalproject.base.BaseFragment
import com.example.androidmobilebootcampfinalproject.databinding.FragmentCurrentWeatherBinding
import com.example.androidmobilebootcampfinalproject.databinding.FragmentWeatherDetailBinding
import com.example.androidmobilebootcampfinalproject.models.Hour
import com.example.androidmobilebootcampfinalproject.network.ConnectionLiveData2
import com.example.androidmobilebootcampfinalproject.utils.showToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class WeatherDetailFragment : BaseFragment<WeatherDetailViewModel, FragmentWeatherDetailBinding>() {

    override var _dataBinding: FragmentWeatherDetailBinding? = null
    val dataBinding get() = _dataBinding!!

    var location: String = ""

    var hourForecasts: ArrayList<Hour> = ArrayList()

    private val connectionLiveData2: ConnectionLiveData2 by inject()

    override val viewModel: WeatherDetailViewModel by viewModel()

    override fun getLayoutID() = R.layout.fragment_weather_detail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = this.arguments
        this.location = args?.getString("location")!!

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun prepareView() {

        dataBinding.locationName.text = this.location

        viewModel.prepareWeatherDetails(this.location)

    }

    override fun observeLiveData() {

        //observing the internet connection and setting what will be viewed on the screen accordingly
        connectionLiveData2.observe(viewLifecycleOwner, {
            viewModel.prepareWeatherDetails(this.location)
        })


        val weatherDetailsLoadingToast = Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT)
        viewModel.onWeatherDetailsLoading.observe(viewLifecycleOwner, {
            weatherDetailsLoadingToast.show()
        })



        viewModel.onWeatherDetailsFetched.observe(viewLifecycleOwner, {

            weatherDetailsLoadingToast.cancel()
            dataBinding.noHourForecasts.visibility = View.GONE
            dataBinding.hourForecastRecycler.visibility = View.VISIBLE

            dataBinding.weatherDetailViewStateModel = it
            dataBinding.executePendingBindings()

            hourForecasts = dataBinding.weatherDetailViewStateModel!!.forecastResponse!!.forecast.forecastDay[0].hour
            dataBinding.hourForecastRecycler.layoutManager = LinearLayoutManager(requireContext())
            dataBinding.hourForecastRecycler.adapter = HourForecastAdapter(hourForecasts)
        })



        viewModel.onWeatherDetailsError.observe(viewLifecycleOwner, {
            weatherDetailsLoadingToast.cancel()
            dataBinding.noHourForecasts.visibility = View.VISIBLE
            dataBinding.hourForecastRecycler.visibility = View.GONE
            showToast("No internet connection, cannot display " +
                    "hourly forecasts of the selected location", Toast.LENGTH_LONG)
        })



    }


}
package com.example.androidmobilebootcampfinalproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmobilebootcampfinalproject.R
import com.example.androidmobilebootcampfinalproject.databinding.HourForecastBinding
import com.example.androidmobilebootcampfinalproject.models.Hour

class HourForecastAdapter(
    private val hourForecasts: ArrayList<Hour>
): RecyclerView.Adapter<HourForecastAdapter.HourForecastViewHolder>() {

    inner class HourForecastViewHolder(private val hourForecastBinding: HourForecastBinding):
        RecyclerView.ViewHolder(hourForecastBinding.root){

        fun populate(hourForecast: Hour){
            hourForecastBinding.hour = hourForecast
            hourForecastBinding.executePendingBindings()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourForecastViewHolder {
        return HourForecastViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.hour_forecast,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HourForecastViewHolder, position: Int) {
        val hourForecast = hourForecasts[position]
        holder.apply {
            populate(hourForecast)
        }
    }

    override fun getItemCount(): Int {
        return hourForecasts.size
    }


}


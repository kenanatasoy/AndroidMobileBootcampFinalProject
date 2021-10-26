package com.example.androidmobilebootcampfinalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmobilebootcampfinalproject.R
import com.example.androidmobilebootcampfinalproject.databinding.CurrentWeatherLayoutBinding
import com.example.androidmobilebootcampfinalproject.databinding.NoLocationSelectedBinding
import com.example.androidmobilebootcampfinalproject.models.CurrentResponse

class ViewPagerAdapter(
    currentResponses: MutableList<CurrentResponse>,
    private val clickListener: OnCurrentWeatherDetailsClickHandler
) : RecyclerView.Adapter<ViewPagerAdapter.BaseItemHolder>() {

    companion object {
        private const val NO_LOCATIONS_SELECTED = 1
        private const val CURRENT_FORECAST = 2
    }

    private var currentResponsesList = currentResponses

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder {
        when (viewType) {
            CURRENT_FORECAST -> return CurrentWeatherViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.current_weather_layout,
                    parent,
                    false
                )
            )
            NO_LOCATIONS_SELECTED -> return NoLocationSelectedViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.no_location_selected,
                    parent,
                    false
                )
            )
        }
        return BaseItemHolder(parent)
    }


    override fun onBindViewHolder(holder: BaseItemHolder, position: Int) {
        if (holder is CurrentWeatherViewHolder) {
            holder.bind(currentResponsesList[position])
        } else if (holder is NoLocationSelectedViewHolder) {

        }
    }

    override fun getItemCount(): Int {
        return if (currentResponsesList.size == 0) {
            1
        } else {
            currentResponsesList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentResponsesList.isEmpty()) {
            NO_LOCATIONS_SELECTED
        } else {
            CURRENT_FORECAST
        }
    }

    open class BaseItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class NoLocationSelectedViewHolder(noLocationSelectedBinding: NoLocationSelectedBinding) :
        BaseItemHolder(noLocationSelectedBinding.root)


    inner class CurrentWeatherViewHolder(
        private val currentWeatherLayoutBinding: CurrentWeatherLayoutBinding
    ) : ViewPagerAdapter.BaseItemHolder(currentWeatherLayoutBinding.root),
        View.OnClickListener {

        init {
            currentWeatherLayoutBinding.detailsButton.setOnClickListener(this)
        }

        fun bind(currentResponse: CurrentResponse) {
            currentWeatherLayoutBinding.currentResponse = currentResponse
            currentWeatherLayoutBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onCurrentWeatherDetailsClicked(position)
            }
        }

    }


}




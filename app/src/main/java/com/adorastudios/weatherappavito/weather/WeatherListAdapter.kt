package com.adorastudios.weatherappavito.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adorastudios.weatherappavito.R
import com.adorastudios.weatherappavito.model.Weather

class WeatherListAdapter :
    ListAdapter<Weather, WeatherListAdapter.WeatherViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_weather, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val time = view.findViewById<TextView>(R.id.textViewTimeStamp)
        private val temperature = view.findViewById<TextView>(R.id.textViewTemperature)
        private val humidity = view.findViewById<TextView>(R.id.textViewHumidity)
        private val rainState = view.findViewById<TextView>(R.id.textViewRainState)
        private val rainIcon = view.findViewById<ImageView>(R.id.imageViewRainState)

        fun bind(weather: Weather) {
            time.text = weather.timeString
            if (weather.temperatureAdditional != null) {
                temperature.text =
                    itemView.context.getString(R.string.temperature_min_max, weather.temperature, weather.temperatureAdditional)
                temperature.textSize = 14f
            } else {
                temperature.text =
                    itemView.context.getString(R.string.number_degrees, weather.temperature)
            }
            humidity.text = itemView.context.getString(R.string.number_percentage, weather.humidity)
            rainState.text = weather.rainState
            val imageUrl = itemView.context.getString(R.string.load_image, weather.rainImage)
            rainIcon.load(imageUrl)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }
    }
}
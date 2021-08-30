package com.adorastudios.weatherappavito.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.adorastudios.weatherappavito.MainActivity
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.location.Location.Companion.LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LONGITUDE_STRING
import com.adorastudios.weatherappavito.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(private val dataSource: DataSource, context: Context) : ViewModel() {
    private val weather7dList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weather24hList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weatherNow: MutableLiveData<Weather> = MutableLiveData()
    private val locationString: MutableLiveData<String> = MutableLiveData()
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val weather7D : MutableLiveData<List<Weather>> get() = weather7dList
    val weather24H : MutableLiveData<List<Weather>> get() = weather24hList
    val location : MutableLiveData<String> get() = locationString
    val weather : MutableLiveData<Weather> get() = weatherNow

    fun repetitiveInit() {
        loadLocation()
        loadWeather()
    }

    private fun loadLocation() {
        val lat = MainActivity.sharedPreferences.getFloat(LATITUDE_STRING, 1000f)
        val lon = MainActivity.sharedPreferences.getFloat(LONGITUDE_STRING, 1000f)
        if (lat == 1000f || lon == 1000f) {
            locationString.postValue("Error: LA loaded")
            return
        }
        locationString.postValue(String.format("%1$.2fx%2$.2f", lat, lon))
    }

    private fun loadWeather() {
        viewModelScope.launch {
            val l = dataSource.loadWeather()
            weather24hList.postValue(l.hourly)
            weather7dList.postValue(l.daily)
            weatherNow.postValue(l.curr)
        }
    }

}
package com.adorastudios.weatherappavito.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.weatherappavito.MainActivity
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.location.Location.Companion.LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LONGITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.NAME_STRING
import com.adorastudios.weatherappavito.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(private val dataSource: DataSource) : ViewModel() {
    private val weather7dList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weather24hList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weatherNow: MutableLiveData<Weather> = MutableLiveData()
    private val locationNameString: MutableLiveData<String> = MutableLiveData()
    private val locationCoordinatesString: MutableLiveData<String> = MutableLiveData()

    val weather7D : MutableLiveData<List<Weather>> get() = weather7dList
    val weather24H : MutableLiveData<List<Weather>> get() = weather24hList
    val locationName: MutableLiveData<String> get() = locationNameString
    val locationCoordinates: MutableLiveData<String> get() = locationCoordinatesString
    val weather : MutableLiveData<Weather> get() = weatherNow

    fun repetitiveInit() {
        loadLocation()
        loadWeather()
    }

    private fun loadLocation() {
        val name = MainActivity.sharedPreferences.getString(NAME_STRING, "")
        val lat = MainActivity.sharedPreferences.getFloat(LATITUDE_STRING, 1000f)
        val lon = MainActivity.sharedPreferences.getFloat(LONGITUDE_STRING, 1000f)
        if (lat == 1000f || lon == 1000f) {
            locationNameString.postValue("Error")
            locationCoordinatesString.postValue("")
            return
        }
        locationNameString.postValue(name)
        locationCoordinatesString.postValue(String.format("[%1$.2fx%2$.2f]", lat, lon))
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
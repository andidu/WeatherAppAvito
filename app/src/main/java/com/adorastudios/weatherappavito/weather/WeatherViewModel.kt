package com.adorastudios.weatherappavito.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.weatherappavito.MainActivity
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.data.Failure
import com.adorastudios.weatherappavito.data.Result
import com.adorastudios.weatherappavito.data.Success
import com.adorastudios.weatherappavito.location.Location
import com.adorastudios.weatherappavito.model.Weather
import com.adorastudios.weatherappavito.model.Weather3In1
import kotlinx.coroutines.launch

class WeatherViewModel(private val dataSource: DataSource) : ViewModel() {
    private val weather7dList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weather24hList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weatherNow: MutableLiveData<Weather> = MutableLiveData()
    private val weatherErrorThrowable: MutableLiveData<Throwable> = MutableLiveData()
    private val locationNameString: MutableLiveData<String> = MutableLiveData()
    private val locationCoordinatesString: MutableLiveData<String> = MutableLiveData()

    val weather7D : MutableLiveData<List<Weather>> get() = weather7dList
    val weather24H : MutableLiveData<List<Weather>> get() = weather24hList
    val locationName: MutableLiveData<String> get() = locationNameString
    val locationCoordinates: MutableLiveData<String> get() = locationCoordinatesString
    val weather : MutableLiveData<Weather> get() = weatherNow
    val weatherError : MutableLiveData<Throwable> get() = weatherErrorThrowable

    fun repetitiveInit() :Int {
        val r = loadLocation()
        if (r > 0) {
            loadWeather()
        }
        return r
    }

    private fun loadLocation() : Int{
        val name = MainActivity.sharedPreferences.getString(Location.NAME_STRING, "Your Location")

        val curr = MainActivity.sharedPreferences.getBoolean(Location.CURR_STRING, true)

        if (curr) {
            val currLat = MainActivity.sharedPreferences.getFloat(Location.CURR_LATITUDE_STRING, 1000f)
            val currLon = MainActivity.sharedPreferences.getFloat(Location.CURR_LONGITUDE_STRING, 1000f)
            return if (currLat > 999 || currLon > 999) {
                locationNameString.postValue("Error")
                locationCoordinatesString.postValue("")
                -1
            } else {
                locationNameString.postValue(name)
                locationCoordinatesString.postValue(String.format("[%1$.2fx%2$.2f]", currLat, currLon))
                1
            }
        } else {
            val lat = MainActivity.sharedPreferences.getFloat(Location.LATITUDE_STRING, 1000f)
            val lon = MainActivity.sharedPreferences.getFloat(Location.LONGITUDE_STRING, 1000f)
            return if (lat > 999 || lon > 999) {
                locationNameString.postValue("Error")
                locationCoordinatesString.postValue("")
                -2
            } else {
                locationNameString.postValue(name)
                locationCoordinatesString.postValue(String.format("[%1$.2fx%2$.2f]", lat, lon))
                1
            }
        }


    }

    private fun loadWeather() {
        viewModelScope.launch {
            handleResult(dataSource.loadWeather())
        }
    }

    private fun handleResult(result: Result<Weather3In1>) {
        when (result) {
            is Success -> {
                weather24hList.postValue(result.data.hourly)
                weather7dList.postValue(result.data.daily)
                weatherNow.postValue(result.data.curr)
            }
            is Failure -> {
                weatherErrorThrowable.postValue(result.exception)
            }
        }
    }

}
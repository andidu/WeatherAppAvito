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
import com.adorastudios.weatherappavito.model.Weather3In1
import kotlinx.coroutines.launch

class WeatherViewModel(private val dataSource: DataSource) : ViewModel() {
    private val _weatherResponse: MutableLiveData<WeatherResponseState> = MutableLiveData()
    private val _location: MutableLiveData<LocationResponseState> = MutableLiveData()

    val weatherResponse: MutableLiveData<WeatherResponseState> get() = _weatherResponse
    val location: MutableLiveData<LocationResponseState> get() = _location

    companion object {
        const val LOADING_RESPONSE_OK = 1
        const val LOADING_RESPONSE_ERROR_SELECTED_LOCATION = -2
        const val LOADING_RESPONSE_ERROR_CURRENT_LOCATION = -1
    }

    fun repetitiveInit(): Int {
        val r = loadLocation()
        if (r > 0) {
            loadWeather()
        }
        return r
    }

    private fun loadLocation(): Int {
        val name = MainActivity.sharedPreferences.getString(Location.NAME_STRING, "Your Location")
        val curr = MainActivity.sharedPreferences.getBoolean(Location.CURR_STRING, true)

        if (curr) {
            val currLat =
                MainActivity.sharedPreferences.getFloat(Location.CURR_LATITUDE_STRING, 1000f)
            val currLon =
                MainActivity.sharedPreferences.getFloat(Location.CURR_LONGITUDE_STRING, 1000f)
            return if (currLat > 999 || currLon > 999) {
                _location.postValue(LocationResponseState.LocationError)
                LOADING_RESPONSE_ERROR_CURRENT_LOCATION
            } else {
                _location.postValue(
                    LocationResponseState.LocationOK(
                        name = name ?: "",
                        latitude = currLat,
                        longitude = currLon
                    )
                )
                LOADING_RESPONSE_OK
            }
        } else {
            val lat = MainActivity.sharedPreferences.getFloat(Location.LATITUDE_STRING, 1000f)
            val lon = MainActivity.sharedPreferences.getFloat(Location.LONGITUDE_STRING, 1000f)
            return if (lat > 999 || lon > 999) {
                _location.postValue(LocationResponseState.LocationError)
                LOADING_RESPONSE_ERROR_SELECTED_LOCATION
            } else {
                _location.postValue(
                    LocationResponseState.LocationOK(
                        name = name ?: "",
                        latitude = lat,
                        longitude = lon
                    )
                )
                LOADING_RESPONSE_OK
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
                _weatherResponse.postValue(
                    WeatherResponseState.WeatherOK(
                        weather24h = result.data.hourly,
                        weather7d = result.data.daily,
                        weatherNow = result.data.curr
                    )
                )
            }
            is Failure -> {
                _weatherResponse.postValue(
                    WeatherResponseState.WeatherError(
                        result.exception
                    )
                )
            }
        }
    }

}
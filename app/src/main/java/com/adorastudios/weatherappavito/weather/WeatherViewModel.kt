package com.adorastudios.weatherappavito.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.adorastudios.weatherappavito.R
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(private val dataSource: DataSource, context: Context) : ViewModel() {
    private val weather7dList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weather24hList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weatherNow: MutableLiveData<Weather> = MutableLiveData()
    private val locationString: MutableLiveData<String> = MutableLiveData()
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val NAME_STRING = context.getString(R.string.shared_preferences_name)
    private val ZIP_STRING = context.getString(R.string.shared_preferences_zip)

    val weather7D : MutableLiveData<List<Weather>> get() = weather7dList
    val weather24H : MutableLiveData<List<Weather>> get() = weather24hList
    val location : MutableLiveData<String> get() = locationString
    val weather : MutableLiveData<Weather> get() = weatherNow

    init {
        loadWeather()
    }

    fun repetitiveInit() {
        loadLocation()
    }

    private fun loadLocation() {
        var str = sharedPreferences.getString(NAME_STRING, "")
        if (str == "") {
            str = sharedPreferences.getString(ZIP_STRING, "")
        }
        locationString.postValue(str)
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
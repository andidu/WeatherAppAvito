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
    private val locationString: MutableLiveData<String> = MutableLiveData()
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val NAME_STRING = context.getString(R.string.shared_preferences_name)
    private val ZIP_STRING = context.getString(R.string.shared_preferences_zip)

    val weather7D : MutableLiveData<List<Weather>> get() = weather7dList
    val weather24H : MutableLiveData<List<Weather>> get() = weather24hList
    val location : MutableLiveData<String> get() = locationString

    init {
        loadWeather7d()
        loadWeather24h()
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

    private fun loadWeather24h() {
        viewModelScope.launch {
            weather24hList.postValue(dataSource.loadWeather24H())
        }
    }

    private fun loadWeather7d() {
        viewModelScope.launch {
            weather7dList.postValue(dataSource.loadWeather7D())
        }
    }

}
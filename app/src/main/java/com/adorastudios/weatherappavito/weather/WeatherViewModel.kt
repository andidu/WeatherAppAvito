package com.adorastudios.weatherappavito.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(private val dataSource: DataSource) : ViewModel() {
    private val weather7dList: MutableLiveData<List<Weather>> = MutableLiveData()
    private val weather24hList: MutableLiveData<List<Weather>> = MutableLiveData()

    val weather7D : MutableLiveData<List<Weather>> get() = weather7dList
    val weather24H : MutableLiveData<List<Weather>> get() = weather24hList

    init {
        loadWeather7d()
        loadWeather24h()
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
package com.adorastudios.weatherappavito.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adorastudios.weatherappavito.data.DataSource

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory (
    private val dataSource: DataSource
        ): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        WeatherViewModel(dataSource) as T
}
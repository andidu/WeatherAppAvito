package com.adorastudios.weatherappavito.weather

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adorastudios.weatherappavito.data.DataSource

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory (
    private val dataSource: DataSource,
    private val context: Context
        ): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        WeatherViewModel(dataSource, context) as T
}
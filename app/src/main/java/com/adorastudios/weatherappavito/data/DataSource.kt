package com.adorastudios.weatherappavito.data

import com.adorastudios.weatherappavito.model.Weather

interface DataSource {
    suspend fun loadWeather24H(): List<Weather>
    suspend fun loadWeather7D(): List<Weather>
}
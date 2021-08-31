package com.adorastudios.weatherappavito.data

import com.adorastudios.weatherappavito.model.Weather3In1

interface DataSource {
    suspend fun loadWeather(): Result<Weather3In1>
}
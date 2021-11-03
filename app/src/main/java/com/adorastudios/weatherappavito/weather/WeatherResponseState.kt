package com.adorastudios.weatherappavito.weather

import com.adorastudios.weatherappavito.model.Weather

sealed class WeatherResponseState {

    class WeatherOK(
        val weather7d: List<Weather>,
        val weather24h: List<Weather>,
        val weatherNow: Weather
    ) : WeatherResponseState()

    class WeatherError(
        val error: Throwable
    ) : WeatherResponseState()
}
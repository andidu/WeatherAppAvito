package com.adorastudios.weatherappavito.data

import com.adorastudios.weatherappavito.data.retrofit.WeatherApiService
import com.adorastudios.weatherappavito.model.Weather
import com.adorastudios.weatherappavito.model.Weather3In1

class DataSourceImpl(private val api: WeatherApiService) : DataSource {

    companion object {
        val list = listOf(
            "Today",
            "Tomorrow",
            "In 2 days",
            "In 3 days",
            "In 4 days",
            "In 5 days",
            "In 6 days"
        )
    }

    override suspend fun loadWeather(): Weather3In1 {
        val loaded = api.loadWeather()
        var offset: Int
        val weatherNow = loaded.weatherResponse.let {
            offset = it.time / (3600 * 24)
            Weather(
                it.time,
                null,
                it.temperature - 273,
                null,
                it.humidity,
                it.rainStateResponses[0].rainState,
                it.rainStateResponses[0].icon
            )
        }
        val weather24 = loaded.hourlyResponses.map { response ->
            Weather(
                response.time,
                String.format("%1$02d:%2$02d", response.time / 3600 % 24, response.time / 60 % 60),
                response.temperature - 273,
                null,
                response.humidity,
                response.rainStateResponses[0].rainState,
                response.rainStateResponses[0].icon
            )
        }.take(24)
        val weather7 = loaded.dailyResponses.map { response ->
            Weather(
                response.time,
                list[(response.time / (3600 * 24) - offset) % 7],
                response.temperature.min - 273,
                response.temperature.max - 273,
                response.humidity,
                response.rainStateResponses[0].rainState,
                response.rainStateResponses[0].icon
            )
        }.take(7)

        return Weather3In1(weatherNow, weather24, weather7)
    }
}
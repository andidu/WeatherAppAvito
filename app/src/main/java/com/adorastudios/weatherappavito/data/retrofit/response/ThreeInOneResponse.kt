package com.adorastudios.weatherappavito.data.retrofit.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ThreeInOneResponse (
    @SerialName("current") val weatherResponse: WeatherResponse,
    @SerialName("hourly") val hourlyResponses: List<WeatherResponse>,
    @SerialName("daily") val dailyResponses: List<WeatherDailyResponse>
)
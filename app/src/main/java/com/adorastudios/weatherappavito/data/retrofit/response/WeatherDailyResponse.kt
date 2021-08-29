package com.adorastudios.weatherappavito.data.retrofit.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WeatherDailyResponse (
    @SerialName("temp") val temperature: TemperatureResponse,
    @SerialName("humidity") val humidity: Int,
    @SerialName("weather") val rainStateResponses : List<RainStateResponse>,
    @SerialName("dt") val time : Int
)
package com.adorastudios.weatherappavito.data.retrofit.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TemperatureResponse (
    @SerialName("min") val min: Double,
    @SerialName("max") val max: Double
)
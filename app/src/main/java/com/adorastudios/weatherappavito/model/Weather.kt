package com.adorastudios.weatherappavito.model

data class Weather(
    val time: Int,
    val timeString: String?,
    val temperature: Double,
    val temperatureAdditional: Double? = null,
    val humidity: Int,
    val rainState: String,
    val rainImage: String
)
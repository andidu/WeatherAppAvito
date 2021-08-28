package com.adorastudios.weatherappavito.model

data class Weather(
    val time: String,
    val temperature: Int,
    val rainState: String,
    val humidity: Double
)
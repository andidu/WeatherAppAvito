package com.adorastudios.weatherappavito.model

data class Weather3In1(
    val curr: Weather,
    val hourly: List<Weather>,
    val daily: List<Weather>
)
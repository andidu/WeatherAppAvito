package com.adorastudios.weatherappavito.weather


sealed class LocationResponseState {

    class LocationOK(
        val name: String,
        val latitude: Float,
        val longitude: Float
    ) : LocationResponseState()

    object LocationError : LocationResponseState()
}
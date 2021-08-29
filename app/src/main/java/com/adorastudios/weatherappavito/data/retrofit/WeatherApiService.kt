package com.adorastudios.weatherappavito.data.retrofit

import com.adorastudios.weatherappavito.data.retrofit.response.ThreeInOneResponse
import retrofit2.http.GET

interface WeatherApiService {
    @GET("onecall")
    suspend fun loadWeather() : ThreeInOneResponse
}
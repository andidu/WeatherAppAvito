package com.adorastudios.weatherappavito.data.network_module

import com.adorastudios.weatherappavito.data.retrofit.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkModule {
    private val baseUrl = "https://api.openweathermap.org/data/"
    private val version = "2.5/"

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val contentType = "application/json".toMediaType()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(LocationInterceptor())
        .addInterceptor(AdditionalParamInterceptor())
        .addInterceptor(ApiKeyInterceptor())
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(baseUrl + version)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(httpClient)

    private val retrofit = retrofitBuilder.build()

    val api: WeatherApiService by lazy { retrofit.create(WeatherApiService::class.java) }
}
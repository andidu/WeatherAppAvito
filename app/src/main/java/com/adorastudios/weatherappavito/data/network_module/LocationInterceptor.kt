package com.adorastudios.weatherappavito.data.network_module

import com.adorastudios.weatherappavito.MainActivity
import com.adorastudios.weatherappavito.location.Location.Companion.LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LONGITUDE_STRING
import okhttp3.Interceptor
import okhttp3.Response

class LocationInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val lat = MainActivity.sharedPreferences.getFloat(LATITUDE_STRING, 1000f)
        val lon = MainActivity.sharedPreferences.getFloat(LONGITUDE_STRING, 1000f)

        val origin = chain.request()
        val urlBuilder = origin.url.newBuilder()
        val url = urlBuilder
            .addQueryParameter("lat", "" + lat)
            .addQueryParameter("lon", "" + lon)
            .build()

        val requestBuilder = origin.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
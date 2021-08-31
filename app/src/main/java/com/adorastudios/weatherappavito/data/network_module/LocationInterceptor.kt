package com.adorastudios.weatherappavito.data.network_module

import com.adorastudios.weatherappavito.MainActivity
import com.adorastudios.weatherappavito.location.Location.Companion.CURR_LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.CURR_LONGITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.CURR_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LONGITUDE_STRING
import okhttp3.Interceptor
import okhttp3.Response

class LocationInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val curr = MainActivity.sharedPreferences.getBoolean(CURR_STRING, true)

        val origin = chain.request()
        val urlBuilder = origin.url.newBuilder()
        val url = if (curr) {
            val currLat = MainActivity.sharedPreferences.getFloat(CURR_LATITUDE_STRING, 1000f)
            val currLon = MainActivity.sharedPreferences.getFloat(CURR_LONGITUDE_STRING, 1000f)
            urlBuilder
                .addQueryParameter("lat", "" + currLat)
                .addQueryParameter("lon", "" + currLon)
                .build()
        } else {
            val lat = MainActivity.sharedPreferences.getFloat(LATITUDE_STRING, 1000f)
            val lon = MainActivity.sharedPreferences.getFloat(LONGITUDE_STRING, 1000f)
            urlBuilder
                .addQueryParameter("lat", "" + lat)
                .addQueryParameter("lon", "" + lon)
                .build()
        }

        val requestBuilder = origin.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
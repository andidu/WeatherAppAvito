package com.adorastudios.weatherappavito.data.network_module

import okhttp3.Interceptor
import okhttp3.Response

class LocationInterceptor: Interceptor {

    companion object {
        const val LOCATION = "Moscow"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val urlBuilder = origin.url.newBuilder()
        val url = urlBuilder
            .addQueryParameter("lat", "33.44")
            .addQueryParameter("lon", "-94.04")
            .build()

        val requestBuilder = origin.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
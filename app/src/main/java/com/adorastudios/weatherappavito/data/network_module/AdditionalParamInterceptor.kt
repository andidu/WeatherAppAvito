package com.adorastudios.weatherappavito.data.network_module

import okhttp3.Interceptor
import okhttp3.Response

class AdditionalParamInterceptor: Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val urlBuilder = origin.url.newBuilder()
        val url = urlBuilder
            .addQueryParameter("exclude", "minutely,allerts")
            .build()

        val requestBuilder = origin.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
package com.adorastudios.weatherappavito.data.retrofit.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RainStateResponse(
    @SerialName("main") val rainState: String,
    @SerialName("icon") val icon : String
)
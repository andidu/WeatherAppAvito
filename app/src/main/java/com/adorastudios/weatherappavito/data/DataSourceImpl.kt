package com.adorastudios.weatherappavito.data

import com.adorastudios.weatherappavito.model.Weather

class DataSourceImpl : DataSource {
    override suspend fun loadWeather24H(): List<Weather> {
        return listOf(
            Weather("00:00:00", 13, "Clouds", 45.5),
            Weather("01:00:00", 14, "Clouds", 45.5),
            Weather("02:00:00", 17, "Clouds", 45.5),
            Weather("03:00:00", 18, "Thunder", 46.5),
            Weather("04:00:00", 11, "Clouds", 45.5),
            Weather("05:00:00", 8, "Clouds", 42.5),
            Weather("06:00:00", 3, "Clouds", 45.5),
            Weather("07:00:00", 13, "Clouds", 45.5),
            Weather("08:00:00", 18, "Clouds", 99.5),
            Weather("09:00:00", 18, "Sun", 45.5),
            Weather("10:00:00", 19, "Clouds", 88.5),
            Weather("11:00:00", 13, "Clouds", 45.5),
            Weather("12:00:00", 20, "Clouds", 45.5),
            Weather("13:00:00", 15, "Clouds", 45.5),
            Weather("14:00:00", 15, "Sun", 45.5),
            Weather("15:00:00", 16, "Clouds", 45.5),
            Weather("16:00:00", 18, "Clouds", 45.5),
            Weather("17:00:00", 15, "Clouds", 44.5),
            Weather("18:00:00", 24, "Clouds", 47.5),
            Weather("19:00:00", 24, "Rain", 45.5),
            Weather("20:00:00", 25, "Clouds", 45.5),
            Weather("21:00:00", 19, "Clouds", 45.5),
            Weather("22:00:00", 26, "Clouds", 0.5),
            Weather("23:00:00", 10, "Clouds", 45.5)
        )
    }

    override suspend fun loadWeather7D(): List<Weather> {
        return listOf(
            Weather("1.06.21", 14, "Clouds", 45.5),
            Weather("2.06.21", 17, "Clouds", 45.5),
            Weather("3.06.21", 18, "Thunder", 46.5),
            Weather("4.06.21", 11, "Clouds", 45.5),
            Weather("5.06.21", 8, "Clouds", 42.5),
            Weather("6.06.21", 3, "Clouds", 45.5),
            Weather("7.06.21", 13, "Clouds", 45.5)
        )
    }
}
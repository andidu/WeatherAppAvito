package com.adorastudios.weatherappavito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adorastudios.weatherappavito.city.CityFragment
import com.adorastudios.weatherappavito.weather.WeatherFragment

class MainActivity : AppCompatActivity(),
    WeatherFragment.IToCityFragment,
    CityFragment.IBackFromCityFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            toWeatherFragment()
        }
    }

    private fun toWeatherFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment,
                WeatherFragment.newInstance(),
                WeatherFragment::class.java.simpleName
            )
            .commit()
    }

    override fun toCityFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment,
                CityFragment.newInstance(),
                CityFragment::class.java.simpleName
            )
            .addToBackStack("transition:${CityFragment::class.java.simpleName}")
            .commit()
    }

    override fun backFromCityFragment() {
        supportFragmentManager.popBackStack()
    }
}
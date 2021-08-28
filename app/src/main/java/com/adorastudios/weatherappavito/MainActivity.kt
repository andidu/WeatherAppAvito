package com.adorastudios.weatherappavito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adorastudios.weatherappavito.weather.WeatherFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment,
                WeatherFragment.newInstance(),
                WeatherFragment::class.java.simpleName
            )
            .commit()
    }
}
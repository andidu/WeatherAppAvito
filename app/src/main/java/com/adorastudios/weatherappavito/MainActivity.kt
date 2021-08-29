package com.adorastudios.weatherappavito

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.adorastudios.weatherappavito.city.CityFragment
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.data.DataSourceImpl
import com.adorastudios.weatherappavito.data.DataSourceProvider
import com.adorastudios.weatherappavito.data.network_module.NetworkModule
import com.adorastudios.weatherappavito.weather.WeatherFragment

class MainActivity : AppCompatActivity(),
    WeatherFragment.IToCityFragment,
    CityFragment.IBackFromCityFragment,
    DataSourceProvider {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }

    private lateinit var dataSource: DataSource
    private lateinit var networkModule: NetworkModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        networkModule = NetworkModule()
        dataSource = DataSourceImpl(networkModule.api)

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

    override fun provideDataSource(): DataSource {
        return dataSource
    }
}
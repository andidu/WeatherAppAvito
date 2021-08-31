package com.adorastudios.weatherappavito

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.adorastudios.weatherappavito.city.CityFragment
import com.adorastudios.weatherappavito.data.DataSource
import com.adorastudios.weatherappavito.data.DataSourceImpl
import com.adorastudios.weatherappavito.data.DataSourceProvider
import com.adorastudios.weatherappavito.data.network_module.NetworkModule
import com.adorastudios.weatherappavito.location.Location.Companion.CURR_LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.CURR_LONGITUDE_STRING
import com.adorastudios.weatherappavito.weather.WeatherFragment

class MainActivity : AppCompatActivity(),
    WeatherFragment.IToCityFragment,
    CityFragment.IBackFromCityFragment,
    DataSourceProvider {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        private val LOCATION_CODE = 43
    }

    private lateinit var mLocationManager: LocationManager
    private lateinit var dataSource: DataSource
    private lateinit var networkModule: NetworkModule
    private var loadWeatherFragment: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        loadWeatherFragment = savedInstanceState == null
        mLocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not yet granted
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_CODE
            )
            return
        } else {
            // Permission had been granted previously
            loadCurrentLocation()
            launchApp()
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    loadCurrentLocation()
                }
                launchApp()
                return
            }
        }
    }

    private fun getLastKnownLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val providers = mLocationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val l = mLocationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
            return bestLocation
        }
        return null
    }

    private fun loadCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location: Location? = getLastKnownLocation()
            if (location != null) {
                sharedPreferences.edit()
                    .putFloat(CURR_LATITUDE_STRING, location.latitude.toFloat())
                    .putFloat(CURR_LONGITUDE_STRING, location.longitude.toFloat())
                    .apply()
            }
        }
    }

    private fun launchApp() {
        networkModule = NetworkModule()
        dataSource = DataSourceImpl(networkModule.api)

        if (loadWeatherFragment) {
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
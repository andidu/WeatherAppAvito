package com.adorastudios.weatherappavito.city

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adorastudios.weatherappavito.MainActivity
import com.adorastudios.weatherappavito.location.Location.Companion.CURR_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LATITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.LONGITUDE_STRING
import com.adorastudios.weatherappavito.location.Location.Companion.NAME_STRING

class CityViewModel (context: Context): ViewModel() {

    private val geocoder = Geocoder(context)

    val resultLongitude : MutableLiveData<Double> = MutableLiveData()
    val resultLatitude : MutableLiveData<Double> = MutableLiveData()

    fun setName(name: String) : Boolean{

        val list = geocoder.getFromLocationName(name, 1)
        if (list.isEmpty() || !list[0].hasLatitude() || !list[0].hasLongitude()) {
            return false
        }
        resultLongitude.postValue(list[0].longitude)
        resultLatitude.postValue(list[0].latitude)
        return true
    }

    fun save(name : String) {
        val longitude = resultLongitude.value?.toFloat() ?: return
        val latitude = resultLatitude.value?.toFloat() ?: return
        MainActivity.sharedPreferences.edit()
            .putFloat(LONGITUDE_STRING, longitude)
            .putFloat(LATITUDE_STRING, latitude)
            .putString(NAME_STRING, name)
            .putBoolean(CURR_STRING, false)
            .apply()
    }

    fun saveDefault() {
        MainActivity.sharedPreferences.edit()
            .remove(LONGITUDE_STRING)
            .remove(LATITUDE_STRING)
            .remove(NAME_STRING)
            .remove(CURR_STRING)
            .apply()
    }
}
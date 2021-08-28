package com.adorastudios.weatherappavito.city

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.lifecycle.ViewModel

class CityViewModel (context: Context): ViewModel() {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val NAME_STRING = "name"
    private val ZIP_STRING = "zip"

    fun setZip(zip: String) {
        sharedPreferences.edit()
            .putString(ZIP_STRING, zip)
            .remove(NAME_STRING)
            .apply()
    }

    fun setName(name: String) {
        sharedPreferences.edit()
            .putString(NAME_STRING, name)
            .remove(ZIP_STRING)
            .apply()
    }
}
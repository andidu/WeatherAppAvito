package com.adorastudios.weatherappavito.city

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import com.adorastudios.weatherappavito.R

class CityViewModel (context: Context): ViewModel() {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val NAME_STRING = context.getString(R.string.shared_preferences_name)
    private val ZIP_STRING = context.getString(R.string.shared_preferences_zip)
    
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
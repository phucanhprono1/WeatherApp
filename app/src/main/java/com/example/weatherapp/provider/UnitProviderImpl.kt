package com.example.weatherapp.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

const val UNIT_SYSTEM = "UNIT_SYSTEM"
class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {
    private val appContext = context.applicationContext
    private val preferences1: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}
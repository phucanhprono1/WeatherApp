package com.example.weatherapp.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"
class LocationProviderImpl (
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
):  LocationProvider{

    private val appContext = context.applicationContext
    override suspend fun hasLocationChanged(lastWeatherLocation: LocationKeyResponse): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getPreferredLocationString(): String {

//        try {
//            val deviceLocation = getLastDeviceLocation().await()
//                ?: return "${getCustomLocationName()}"
//            return "${deviceLocation.latitude},${deviceLocation.longitude}"
//        } catch (e: LocationPermissionNotGrantedException) {
//            return "${getCustomLocationName()}"
//        }
        return ""
    }



//    @SuppressLint("MissingPermission")
//    private fun getLastDeviceLocation(): Deferred<Location?> {
//        return if (hasLocationPermission())
//            fusedLocationProviderClient.lastLocation.asDeferred()
//        else
//            throw LocationPermissionNotGrantedException()
//    }
//    private fun hasLocationPermission(): Boolean {
//        return ContextCompat.checkSelfPermission(appContext,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//    }

}
@file:Suppress("DEPRECATION")

package com.example.weatherapp

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherapp.adapter.FragmentAdapter
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.fragment.currentweather.CurrentWeatherFragment
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentList : ArrayList<Fragment>
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        fragmentAdapter = FragmentAdapter(this)
        fragmentAdapter.addFragment(CurrentWeatherFragment())
        fragmentAdapter.addFragment(CurrentWeatherFragment())

//        val pageTransformer = Horizontal3DPageTransformer()
        binding.viewPager2Main.adapter = fragmentAdapter
//        binding.viewPager2Main.setPageTransformer(pageTransformer)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLocationAndFetchLocationKey()
        }
    }

    private fun getLocationAndFetchLocationKey() {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Sử dụng Coroutine để gọi API và xử lý kết quả
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val location = getLastKnownLocation()
                    if (location != null) {
                        val response = ServiceFactory.createLocationApi().getLocationKey(ServiceFactory.API_KEY, "${location.latitude},${location.longitude}")
                        handleLocationResponse(response)
                    } else {
                        // Xử lý khi không lấy được vị trí
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                    // Xử lý khi gọi API bị lỗi
                }

            }
        }
    }

    private suspend fun getLastKnownLocation(): Location? = suspendCoroutine { continuation ->
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where permissions are not granted
            continuation.resume(null)
            return@suspendCoroutine
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                continuation.resume(location)
            } else {
                continuation.resumeWithException(IllegalStateException("Last known location is null"))
            }
        }.addOnFailureListener { exception: Exception ->
            continuation.resumeWithException(exception)
        }


    }

    private fun handleLocationResponse(response: Response<LocationKeyResponse>) {
        if (response.isSuccessful && response.body() != null) {
            val locationResponse = response.body()!!
            val locationKey = locationResponse.Key

            // Lưu Location Key vào SharedPreferences
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            sharedPreferences.edit().putString("LOCATION_KEY", locationKey).apply()

            // Do something with the locationKey
        } else {
            // Handle error
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
@file:Suppress("DEPRECATION")

package com.example.weatherapp.ui

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.adapter.FragmentAdapter
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.config.StaticConfig
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.fragment.currentweather.CurrentWeatherFragment

import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.transformer.Horizontal3DPageTransformer
import com.example.weatherapp.transformer.LinearTemp3DPageTransformer

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var fragmentList : ArrayList<Fragment>
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationName = ""

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    val pageTransformer = Horizontal3DPageTransformer()

    private var previousScrollPosition = 0

    // Add a reference to the current fragment
    private var currentFragment: CurrentWeatherFragment? = null
    private var currentVisibleFragmentPosition: Int = 0

    // ViewPager2.OnPageChangeCallback for handling scroll synchronization
    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                // Mark the page as idle in the Horizontal3DPageTransformer
                pageTransformer.setPageIdle(true)
                // Force the transformPage method to be called to update the current fragment's view
                binding.viewPager2Main.post { binding.viewPager2Main.adapter?.notifyDataSetChanged() }
            } else {
                // Mark the page as not idle during the transition
                pageTransformer.setPageIdle(false)
            }
        }
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (currentVisibleFragmentPosition >= 0) {
                val oldVisibleFragment = fragmentAdapter.getFragmentAtPosition(currentVisibleFragmentPosition)
                if (oldVisibleFragment is CurrentWeatherFragment) {
                    oldVisibleFragment.hideTemperatureLayout()
                }
            }

            // Update the Toolbar title with the location name of the selected fragment
            binding.titleTextView.text = fragmentAdapter.getLocationNameAtPosition(position)

            // Show the temperature layout for the selected fragment
            val selectedFragment = fragmentAdapter.getFragmentAtPosition(position)
            if (selectedFragment is CurrentWeatherFragment) {
                selectedFragment.showTemperatureLayout()
            }

            // Update the currentVisibleFragmentPosition
            currentVisibleFragmentPosition = position
            // Update the Toolbar title with the location name of the selected fragment
            binding.titleTextView.text = fragmentAdapter.getLocationNameAtPosition(position)

        }
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Calculate the total scroll offset for each fragment
            val totalScrollOffset = binding.viewPager2Main.width * position + positionOffsetPixels

            // Get the current fragment at the given position
            val fragment = fragmentAdapter.getFragmentAtPosition(position)

            // If the current fragment is a CurrentWeatherFragment, update its scroll position
            if (fragment is CurrentWeatherFragment) {
                fragment.setScrollPosition(totalScrollOffset)
                // Store the current fragment to be used for synchronization in the next scroll event
                currentFragment = fragment
            } else {
                // If the current fragment is not a CurrentWeatherFragment,
                // check if we have a stored reference to a CurrentWeatherFragment
                // and update its scroll position to synchronize the scrolling
                currentFragment?.setScrollPosition(previousScrollPosition)
            }

            // Store the scroll position for synchronization in the next scroll event
            previousScrollPosition = totalScrollOffset
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
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
        fragmentAdapter = FragmentAdapter(this)
//        if(sharedPreferences.getString("LOCATION_NAME", "") != "") {
        locationName = sharedPreferences.getString("LOCATION_NAME", "")!!
        fragmentAdapter.addFragment(CurrentWeatherFragment(), locationName)
        fragmentAdapter.addFragment(CurrentWeatherFragment(), locationName)
        fragmentAdapter.addFragment(CurrentWeatherFragment(), locationName)
    //}

        binding.rightMenuButton.setOnClickListener {
            showPopUpMenu(it)
        }

        binding.viewPager2Main.adapter = fragmentAdapter

        binding.viewPager2Main.adapter = fragmentAdapter
        binding.viewPager2Main.setPageTransformer(pageTransformer)
        binding.viewPager2Main.registerOnPageChangeCallback(viewPagerCallback)


        binding.circleIndicator.setViewPager(binding.viewPager2Main)


    }
    fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }

                R.id.action_share -> true
                else -> false
            }
        }
        popupMenu.show()
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
            locationName = locationResponse.LocalizedName
            sharedPreferences.edit().putString("LOCATION_NAME", locationResponse.LocalizedName).apply()

            // Lưu Location Key vào SharedPreferences
            sharedPreferences.edit().putString("LOCATION_KEY", locationKey).apply()
            sharedPreferences.edit().putString("TIME_ZONE", locationResponse.TimeZone.Name).apply()
            StaticConfig.locationKey = locationKey
            StaticConfig.tz_id = locationResponse.TimeZone.Name

            // Do something with the locationKey
        } else {
            // Handle error
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
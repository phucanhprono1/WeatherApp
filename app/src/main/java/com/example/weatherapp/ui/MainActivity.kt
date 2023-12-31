

package com.example.weatherapp.ui

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.adapter.FragmentAdapter
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.config.StaticConfig
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.provider.KeyName
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.ui.fragment.currentweather.CurrentWeatherFragment

import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.room.LocationDao
import com.example.weatherapp.transformer.Horizontal3DPageTransformer
import com.example.weatherapp.ui.add_location.location_list.LocationList

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    public lateinit var binding: ActivityMainBinding

    //    private lateinit var fragmentList : ArrayList<Fragment>
    public lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationName = ""

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var locationDao: LocationDao
    @Inject
    lateinit var locationRepository: LocationRepository
    val pageTransformer = Horizontal3DPageTransformer()

    private var previousScrollPosition = 0

    // Add a reference to the current fragment
    private var currentFragment: CurrentWeatherFragment? = null
    private var currentVisibleFragmentPosition: Int = 0

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndFetchLocationKey()
                refreshFragments()
            } else {
                // Handle permission denied
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        val keyNameList = ArrayList<KeyName>()
        var keyName: KeyName? = null
        val name = sharedPreferences.getString("LOCATION_NAME", "")
        val key = sharedPreferences.getString("LOCATION_KEY", "")
        if (name != null && key != null && key!="0" && key!="") {
            keyName = KeyName(key, name)
            keyNameList.add(keyName)
        }



        locationRepository.getAllCity().let { locations ->
            for (location in locations) {
                val keyName = KeyName(location.Key, location.LocalizedName)
                keyNameList.add(keyName)
            }

        }
        fragmentAdapter.updateDataList(keyNameList)
        //binding.viewPager2Main.setPageTransformer(pageTransformer)

    }
    private fun setCurrentItem(position: Int) {
        binding.viewPager2Main.setCurrentItem(position, true)
    }
    private fun refreshFragments() {
        // Refresh the fragments
        // Retrieve the location keys from SharedPreferences and locationRepository
        val keyNameList = ArrayList<KeyName>()
        var keyName: KeyName? = null
        val name = sharedPreferences.getString("LOCATION_NAME", "")
        val key = sharedPreferences.getString("LOCATION_KEY", "")
        if (name != null && key != null) {
            keyName = KeyName(key, name)
            keyNameList.add(keyName)
        }



        locationRepository.getAllCity().let { locations ->
            for (location in locations) {
                val keyName = KeyName(location.Key, location.LocalizedName)
                keyNameList.add(keyName)
            }

        }
        fragmentAdapter = FragmentAdapter(this, keyNameList)
    }

    // ViewPager2.OnPageChangeCallback for handling scroll synchronization
    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentVisibleFragmentPosition = position
            binding.titleTextView.text = fragmentAdapter.getLocationNameAtPosition(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val totalScrollOffset = position + positionOffset
            val fragment = fragmentAdapter.getFragmentAtPosition(position)
            if (fragment is CurrentWeatherFragment) {
                fragment.setScrollPosition(totalScrollOffset)
            }
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

        val keyNameList = ArrayList<KeyName>()
        var keyName: KeyName? = null
        val name = sharedPreferences.getString("LOCATION_NAME", "")
        val key = sharedPreferences.getString("LOCATION_KEY", "")
        if (name != null && key != null) {
            keyName = KeyName(key, name)
            keyNameList.add(keyName)
        }


        locationRepository.getAllCity().let { locations ->
            for (location in locations) {
                val keyName = KeyName(location.Key, location.LocalizedName)
                keyNameList.add(keyName)
            }

        }
        fragmentAdapter = FragmentAdapter(this, keyNameList)
        fragmentAdapter.updateDataList(keyNameList)
        binding.viewPager2Main.offscreenPageLimit = 10
        binding.viewPager2Main.adapter = fragmentAdapter
        val locationKey = intent.getStringExtra("LOCATION_KEY")
        if (!locationKey.isNullOrEmpty()) {
            val viewPagerPosition = fragmentAdapter.getPositionForLocationKey(locationKey)
            if (viewPagerPosition != -1) {
                binding.viewPager2Main.currentItem = viewPagerPosition
                binding.titleTextView.text = fragmentAdapter.getLocationNameAtPosition(viewPagerPosition)
                binding.viewPager2Main.registerOnPageChangeCallback(viewPagerCallback)
            }
        }
        binding.viewPager2Main.setPageTransformer(pageTransformer)
        binding.viewPager2Main.registerOnPageChangeCallback(viewPagerCallback)
        binding.circleIndicator.setViewPager(binding.viewPager2Main)

        binding.toolbar.menu.findItem(R.id.action_settings).setOnMenuItemClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        binding.toolbar.menu.findItem(R.id.action_share).setOnMenuItemClickListener {
            val screenshotBitmap = captureScreenshot(window.decorView.rootView)
            val imagePath = saveScreenshot(screenshotBitmap, "screenshot.png")

            val intent = Intent(this, ShareActivity::class.java)
            intent.putExtra("screenshotPath", imagePath)
            startActivity(intent)
            true
        }
        // Them Add Location
        binding.leftButton.setOnClickListener {
            val addIntent = Intent(this@MainActivity, LocationList::class.java)
            startActivity(addIntent)
        }


    }
    fun captureScreenshot(view: View): Bitmap {
        val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        view.draw(canvas)
        return screenshot
    }
    fun saveScreenshot(bitmap: Bitmap, filename: String): String {
        val imagePath = File(externalCacheDir, filename)
        val fos = FileOutputStream(imagePath)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return imagePath.absolutePath
    }
    override fun onResume() {
        super.onResume()
        //check permission
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
            fragmentAdapter.notifyDataSetChanged()
            binding.viewPager2Main.registerOnPageChangeCallback(viewPagerCallback)
            binding.circleIndicator.setViewPager(binding.viewPager2Main)
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
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val location = getLastKnownLocation()
                    if (location != null) {
                        val response = ServiceFactory.createLocationApi().getLocationKey(
                            ServiceFactory.API_KEY,
                            "${location.latitude},${location.longitude}"
                        )
                        handleLocationResponse(response)
                    } else {
                        // Xử lý khi không lấy được vị trí
                    }
                } catch (e: Exception) {
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
            sharedPreferences.edit().putString("LOCATION_NAME", locationResponse.LocalizedName)
                .apply()

            // Lưu Location Key vào SharedPreferences
            sharedPreferences.edit().putString("LOCATION_KEY", locationKey).apply()
            sharedPreferences.edit().putString("TIME_ZONE", locationResponse.TimeZone.Name).apply()
            StaticConfig.locationKey = locationKey
            StaticConfig.tz_id = locationResponse.TimeZone.Name

            // Do something with the locationKey
        } else {
            // Handle error
        }
    };

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
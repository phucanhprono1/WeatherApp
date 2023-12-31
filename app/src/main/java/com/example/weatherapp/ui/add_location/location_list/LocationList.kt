package com.example.weatherapp.ui.add_location.location_list

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.LocationRecyclerAdapter
import com.example.weatherapp.databinding.ActivityLocationListBinding
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.LocationRepositoryImpl
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class LocationList : AppCompatActivity(), LocationRecyclerAdapter.OnLocationLongPressListener, LocationRecyclerAdapter.OnLocationClickListener {

    private lateinit var binding: ActivityLocationListBinding
    private lateinit var viewModel: LocationListViewModel


    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var locationRepo : LocationRepositoryImpl
    @Inject
    lateinit var forecastRepository: ForecastRepository
    private lateinit var locationAdapter : LocationRecyclerAdapter
    var listlocation=ArrayList<LocationKeyResponse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Recyclerview cho Danh sách các địa điểm đã đăng ký
        prepareRecyclerView()

        // View Model
        viewModel = ViewModelProvider(this)[LocationListViewModel::class.java]
        viewModel.getRegisteredLocation()
        viewModel.observeLocationLiveData().observe(this, Observer { locationList ->
            locationAdapter.setLocationKeyResponseList(locationList)
        })

        // Setup các nút
        binding.backButton.setOnClickListener{
            viewModel.backToMainMenu(this)
        }

        binding.fab.setOnClickListener{
            viewModel.startSearchingIntent(this)
        }

        binding.deleteButton.setOnClickListener{
            viewModel.deleteLocation(locationAdapter)
            UpdateUI()
        }

    }

    override fun onBackPressed() {
        if (viewModel.getSelectedMode()){
            viewModel.setSelectedMode(false)
            locationAdapter.exitSelectionMode()
            UpdateUI()
        }
        else{
            super.onBackPressed()
        }
    }

    private fun prepareRecyclerView() {
        locationAdapter = LocationRecyclerAdapter(this, forecastRepository, this)
        binding.cityRView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = locationAdapter
        }
    }

    fun UpdateUI(){
        if (viewModel.getSelectedMode()) {
            binding.fab.hide()
            binding.deleteBar.visibility = View.VISIBLE
        } else {
            binding.fab.show()
            binding.deleteBar.visibility = View.GONE
        }
    }

    override fun onLocationLongPress() {
        if(!viewModel.getSelectedMode()) viewModel.setSelectedMode(true)
        UpdateUI()
    }

    override fun onResume() {
        super.onResume()
        val locationList = locationRepo.getAllCity()
        locationAdapter.setLocationKeyResponseList(locationList)
    }

    override fun onLocationClick(location: LocationKeyResponse) {
        Log.d("LocationList", "onLocationClick: ${location.EnglishName}")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("LOCATION_KEY", location.Key)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
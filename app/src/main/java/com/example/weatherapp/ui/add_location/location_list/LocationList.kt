package com.example.weatherapp.ui.add_location.location_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.LocationRecyclerAdapter
import com.example.weatherapp.databinding.ActivityLocationListBinding

class LocationList : AppCompatActivity(), LocationRecyclerAdapter.OnLocationLongPressListener {

    private lateinit var binding: ActivityLocationListBinding
    private lateinit var viewModel: LocationListViewModel
    private var locationAdapter : LocationRecyclerAdapter = LocationRecyclerAdapter(this)

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
        locationAdapter = LocationRecyclerAdapter(this)
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
}
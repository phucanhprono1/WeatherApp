package com.example.weatherapp.ui.add_location.location_list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.adapter.LocationRecyclerAdapter
import com.example.weatherapp.repository.LocationRepositoryImpl
import com.example.weatherapp.response.LocationInfo
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.ui.add_location.search.SearchLocation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    var locationRepo : LocationRepositoryImpl) : ViewModel() {

    private var locationLiveData = MutableLiveData<List<LocationInfo>>()
    private var isSelectedMode : Boolean = false
    fun getRegisteredLocation() {

    }

    fun observeLocationLiveData() : LiveData<List<LocationInfo>> {
        return locationLiveData
    }

    fun backToMainMenu(activity: Activity) {
        activity.finish()
    }

    fun startSearchingIntent(context: Context) {
        val intent = Intent(context, SearchLocation::class.java)
        context.startActivity(intent)
    }

    fun deleteLocation(adapter : LocationRecyclerAdapter){
        val selectedPositions = adapter.getSelectedItems()
        selectedPositions?.let { positions ->
            positions.sortedDescending().forEach { position ->
                locationRepo.deleteCity(adapter.getLocationList()[position])
            }
        }
        adapter.removeSelectedItems()


        isSelectedMode = false
    }

    fun getSelectedMode() : Boolean{
        return isSelectedMode
    }
    fun setSelectedMode(boolean: Boolean){
        isSelectedMode = boolean
    }

}
package com.example.weatherapp.ui.add_location.location_list

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.adapter.LocationRecyclerAdapter
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.ui.add_location.search.SearchLocation


class LocationListViewModel : ViewModel() {

    private var locationLiveData = MutableLiveData<List<LocationKeyResponse>>()
    private var isSelectedMode : Boolean = false
    fun getRegisteredLocation() {

    }

    fun observeLocationLiveData() : LiveData<List<LocationKeyResponse>> {
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
package com.example.weatherapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.provider.KeyName
import com.example.weatherapp.ui.fragment.currentweather.CurrentWeatherFragment
import java.security.Key

class FragmentAdapter(fragmentActivity: FragmentActivity,private var locationNames:ArrayList<KeyName>) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = ArrayList<Fragment>()


    fun addFragment(fragment: Fragment, locationName: KeyName) {
        fragmentList.add(fragment)
        locationNames.add(locationName)
    }

    override fun getItemCount(): Int {
        return locationNames.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (position >= 0 && position < locationNames.size) {
            val fragment = CurrentWeatherFragment.newInstance(locationNames[position].key)
            fragment
        } else {
            Fragment()
        }
    }
    fun getFragmentAtPosition(position: Int): Fragment? {
        return fragmentList.getOrNull(position)
    }
    fun updateDataList(newList: ArrayList<KeyName>) {
        locationNames=newList
    }
    fun getLocationNameAtPosition(position: Int): String {
        return locationNames[position].name
    }
    fun clearFragments() {
        fragmentList.clear()
        locationNames.clear()
    }
}

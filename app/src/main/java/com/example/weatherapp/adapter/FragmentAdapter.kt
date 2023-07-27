package com.example.weatherapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = ArrayList<Fragment>()
    private val locationNames = ArrayList<String>()

    fun addFragment(fragment: Fragment, locationName: String) {
        fragmentList.add(fragment)
        locationNames.add(locationName)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
    fun getFragmentAtPosition(position: Int): Fragment? {
        return fragmentList.getOrNull(position)
    }

    fun getLocationNameAtPosition(position: Int): String {
        return locationNames[position]
    }
}

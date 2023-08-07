package com.example.weatherapp.ui.fragment.currentotherplace

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R

class CurrentWeatherOtherPlaceFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentWeatherOtherPlaceFragment()
    }

    private lateinit var viewModel: CurrentWeatherOtherPlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather_other_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentWeatherOtherPlaceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
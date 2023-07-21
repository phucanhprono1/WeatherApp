package com.example.weatherapp.fragment.currentweather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.WeatherForecastAdapter
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.fragment.ScopedFragment
import com.example.weatherapp.fragment.WeatherViewModel
import kotlinx.coroutines.launch
import kotlin.text.MatchGroupCollection


class CurrentWeatherFragment : ScopedFragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }
    private val viewModelFactory: CurrentWeatherViewModelFactory by lazy {
        CurrentWeatherViewModelFactory(requireContext(), this)
    }
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var binding: FragmentCurrentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(CurrentWeatherViewModel::class.java)
        // TODO: Use the ViewModel

        val transparentColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        binding.cardViewForecast.setCardBackgroundColor(transparentColor)
        bindUi()

    }
    private fun bindUi()= launch {
        val currentweather = viewModel.currentWeather.await()
        if (currentweather != null) {

            binding.tvTemp.text = currentweather.get(0).Temperature.Metric.Value.toString()
            binding.weatherCondition.text = currentweather.get(0).WeatherText


        }
        val forecastweather = viewModel.forecastWeather.await()
        if (forecastweather != null) {
            binding.rvForecast.adapter = WeatherForecastAdapter(forecastweather)
            binding.rvForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}
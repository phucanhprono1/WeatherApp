package com.example.weatherapp.fragment.currentweather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.WeatherForecastAdapter
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.fragment.ScopedFragment
import com.example.weatherapp.fragment.WeatherViewModel
import com.example.weatherapp.networkdata.WeatherNetworkDataSource
import com.example.weatherapp.networkdata.WeatherNetworkDataSourceImpl
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.ForecastRepositoryImpl
import com.example.weatherapp.room.ForecastDatabase
import kotlinx.coroutines.launch
import kotlin.text.MatchGroupCollection


class CurrentWeatherFragment : ScopedFragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }
    private lateinit var forecastRepository: ForecastRepository
    private lateinit var viewModelFactory: CurrentWeatherViewModelFactory
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


        // TODO: Use the ViewModel
        forecastRepository = ForecastRepositoryImpl(
            ForecastDatabase(requireContext()).currentWeatherDao(),
            WeatherNetworkDataSourceImpl(ServiceFactory)
        )
        viewModelFactory = CurrentWeatherViewModelFactory(requireContext(), this, forecastRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(CurrentWeatherViewModel::class.java)
        val transparentColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        binding.cardViewForecast.setCardBackgroundColor(transparentColor)
        bindUi()

    }
    private fun bindUi()= launch {
        val currentweather = viewModel.currentWeather.await()
        currentweather!!.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.tvTemp.text = it.Temperature.toString()
            binding.tvUnitDegree.text = it.Unit
            binding.weatherCondition.text = it.WeatherText

        }

        val forecastweather = viewModel.forecastWeather.await()
        if (forecastweather != null) {
            binding.rvForecast.adapter = WeatherForecastAdapter(forecastweather)
            binding.rvForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}
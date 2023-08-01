package com.example.weatherapp.ui.fragment.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.HourlyForecastAdapter
import com.example.weatherapp.adapter.WeatherForecastAdapter
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.ui.MainActivity
import com.example.weatherapp.ui.fragment.ScopedFragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CurrentWeatherFragment : ScopedFragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }
//    @Inject
//    lateinit var forecastRepository: ForecastRepository
//    @Inject
//    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var viewModelFactory: CurrentWeatherViewModelFactory
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var binding: FragmentCurrentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        binding.rv24hForecast.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.actionMasked) {
                    MotionEvent.ACTION_DOWN -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> rv.parent.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        return binding.root

    }
    fun showTemperatureLayout() {
        binding.linearTemp.visibility = View.VISIBLE
    }

    fun hideTemperatureLayout() {
        binding.linearTemp.visibility = View.INVISIBLE
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this,viewModelFactory).get(CurrentWeatherViewModel::class.java)
        val nonLiveCurrentWeather = viewModel.currentWeatherNonLive


        bindUi()
        if(nonLiveCurrentWeather == null) return
        binding.tvTemp.text = Math.round(nonLiveCurrentWeather.Temperature).toInt().toString()
        binding.tvUnitDegree.text = "°${nonLiveCurrentWeather.Unit}"
        binding.weatherCondition.text = nonLiveCurrentWeather.WeatherText
        binding.textHumidity.text = "${nonLiveCurrentWeather.RelativeHumidity}%"
        binding.textRealFeel.text = "${nonLiveCurrentWeather.RealFeelTemperature}°${nonLiveCurrentWeather.Unit}"
        binding.textUV.text = nonLiveCurrentWeather.UVIndex.toString()
        binding.textPressure.text = "${nonLiveCurrentWeather.pressure}${nonLiveCurrentWeather.pressureUnit}"
        val nonLiveForecastWeather = viewModel.forecastWeatherNonLive
        binding.rvForecast.adapter = WeatherForecastAdapter(nonLiveForecastWeather)
        binding.rvForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val nonLiveHourlyForecast = viewModel.hourlyForecastNonLive
        binding.rv24hForecast.adapter = HourlyForecastAdapter(nonLiveHourlyForecast)
        binding.rv24hForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }
    private val mainActivity: MainActivity?
        get() = activity as? MainActivity

    // Method to set the scroll position for the fragment's ScrollView
    fun setScrollPosition(scrollX: Int) {
        binding.scrollView.scrollTo(scrollX, 0)
    }

    private fun bindUi()= launch {
        val currentweather = viewModel.currentWeather.await()
        currentweather.observe(viewLifecycleOwner) {

            if (it == null) return@observe

            binding.tvTemp.text = Math.round(it.Temperature).toInt().toString()
            binding.tvUnitDegree.text = "°${it.Unit}"
            binding.weatherCondition.text = it.WeatherText
            binding.textHumidity.text = "${it.RelativeHumidity}%"
            binding.textRealFeel.text = "${it.RealFeelTemperature}°${it.Unit}"
            binding.textUV.text = it.UVIndex.toString()
            binding.textPressure.text = "${it.pressure}${it.pressureUnit}"
//                binding.textChanceOfRain.text = "${it.PrecipitationProbability}%"



        }

        val forecastweather = viewModel.forecastWeather.await()
        forecastweather.observe(viewLifecycleOwner) {
            binding.rvForecast.adapter = WeatherForecastAdapter(it)
            binding.rvForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        val hourlyForecast = viewModel.hourlyForecast.await()
        hourlyForecast.observe(viewLifecycleOwner) {
            binding.rv24hForecast.adapter = HourlyForecastAdapter(it)
            binding.rv24hForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

}
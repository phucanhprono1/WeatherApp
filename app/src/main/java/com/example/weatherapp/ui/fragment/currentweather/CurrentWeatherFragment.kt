package com.example.weatherapp.ui.fragment.currentweather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.WeatherForecastAdapter

import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.ui.fragment.ScopedFragment
import com.example.weatherapp.ui.MainActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    }
    private val mainActivity: MainActivity?
        get() = activity as? MainActivity

    // Method to set the scroll position for the fragment's ScrollView
    fun setScrollPosition(scrollX: Int) {
        binding.scrollView.scrollTo(scrollX, 0)
    }

    // Add this method to set the fixed scroll position for the RecyclerView

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
        setupLineChart()

    }
    private fun setupLineChart() {
        // Sample temperature data
        val temperatureData = arrayOf(89f, 91f, 93f, 95f, 96f, 97f, 99f, 98f, 96f, 93f, 91f, 88f)
        val timeData = arrayOf(
            "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
        )
        // Create entries for the line chart
        val entries: MutableList<Entry> = ArrayList()
        for (i in temperatureData.indices) {
            entries.add(Entry(i.toFloat(), temperatureData[i]))
        }

        // Create a dataset for the line chart
        val dataSet = LineDataSet(entries, "Temperature")
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)
        dataSet.color = resources.getColor(R.color.orange)

        // Combine datasets
        val dataSets: MutableList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        // Create LineData and set it to the chart
        val lineData = LineData(dataSets)
        binding.lineChart.data = lineData

        // Customize the X-axis
        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false) // Disable vertical grid lines
//        xAxis.granularity = 1f // Display labels every 1 hour
        val milestoneSpacing = resources.getDimension(com.intuit.sdp.R.dimen._60sdp)
        xAxis.granularity = milestoneSpacing
        // Hide the right Y-axis (temperature values are on the left)
        binding.lineChart.axisRight.isEnabled = false

        // Disable horizontal grid lines
        binding.lineChart.axisLeft.setDrawGridLines(false)

        // Hide X and Y axes
        binding.lineChart.xAxis.isEnabled = false
        binding.lineChart.axisLeft.isEnabled = false
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.axisLeft.setDrawAxisLine(false)
        binding.lineChart.axisRight.setDrawAxisLine(false)
        binding.lineChart.xAxis.setDrawAxisLine(false)

        // Disable chart borders
        binding.lineChart.setDrawBorders(false)
        // Hide description and legend
        binding.lineChart.description.isEnabled = false
        binding.lineChart.legend.isEnabled = false


        // Refresh the chart
        binding.lineChart.invalidate()
    }


}
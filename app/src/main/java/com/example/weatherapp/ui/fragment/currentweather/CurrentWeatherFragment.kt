package com.example.weatherapp.ui.fragment.currentweather

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.HourlyForecastAdapter
import com.example.weatherapp.adapter.WeatherForecastAdapter

import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.provider.NoConnectivityException
import com.example.weatherapp.ui.Detail5DayActivity
import com.example.weatherapp.ui.MainActivity
import com.example.weatherapp.ui.fragment.ScopedFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CurrentWeatherFragment : ScopedFragment() {

    companion object {
        fun newInstance(locationKey: String): CurrentWeatherFragment {
            val args = Bundle().apply {
                putString("locationKey", locationKey)
            }
            return CurrentWeatherFragment().apply {
                arguments = args
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: CurrentWeatherViewModelFactory
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var binding: FragmentCurrentWeatherBinding
    private val mainActivity: MainActivity?
        get() = activity as? MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            getRefresh()
        }
        binding.rv24hForecast.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            private var isScrollingHorizontally = false
            private var startX = 0f
            private var startY = 0f

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isScrollingHorizontally = false
                        startX = e.x
                        startY = e.y
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = e.x - startX
                        val deltaY = e.y - startY

                        // Chỉ xác định là vuốt ngang nếu chưa xác định là vuốt ngang và biến đổi theo trục X lớn hơn biến đổi theo trục Y
                        if (!isScrollingHorizontally && Math.abs(deltaX) > Math.abs(deltaY)) {
                            isScrollingHorizontally = true
                        }

                        mainActivity?.binding?.viewPager2Main?.isUserInputEnabled =
                            !isScrollingHorizontally
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        isScrollingHorizontally = false
                        mainActivity?.binding?.viewPager2Main?.isUserInputEnabled = true
                    }
                }

                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        binding.rv24hForecast.setHasFixedSize(true)
        binding.rv24hForecast.isNestedScrollingEnabled = false
        binding.rv24hForecast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


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


        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        viewModel.key = arguments?.getString("locationKey")!!
        val locationKey = requireArguments().getString("locationKey")
        locationKey?.let {
            val nonLiveCurrentWeather = viewModel.currentWeatherNonLiveByLocationKey(
                it,viewModel.isMetricUnit
            )
            if (nonLiveCurrentWeather != null) {
                binding.tvTemp.text =
                    Math.round(nonLiveCurrentWeather.Temperature).toInt().toString()
                binding.tvUnitDegree.text = "°${nonLiveCurrentWeather.Unit}"
                binding.weatherCondition.text = nonLiveCurrentWeather.WeatherText
                binding.textHumidity.text = "${nonLiveCurrentWeather.RelativeHumidity}%"
                binding.textRealFeel.text =
                    "${nonLiveCurrentWeather.RealFeelTemperature}°${nonLiveCurrentWeather.Unit}"
                binding.textUV.text = nonLiveCurrentWeather.UVIndex.toString()
                binding.textPressure.text =
                    "${nonLiveCurrentWeather.pressure}${nonLiveCurrentWeather.pressureUnit}"
                binding.tvWindDirCurrent.text =
                    convertAbbreviationToFullDirection(nonLiveCurrentWeather.windDirection)
                binding.tvWindSpeedCurrent.text =
                    "${nonLiveCurrentWeather.windSpeed} ${nonLiveCurrentWeather.windSpeedUnit}"
            }
            val nonLiveForecastWeather = viewModel.forecastWeatherNonLiveByLocationKey(it)
            if (!nonLiveForecastWeather.isNullOrEmpty()) {
                binding.minmaxToday.text =
                    "${Math.round(nonLiveForecastWeather[0].minTemperature)}°/${
                        Math.round(nonLiveForecastWeather[0].maxTemperature)
                    }°"
                binding.tvMoreDetails5dayForecast.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(nonLiveForecastWeather[0].link)
                        )
                    )
                }
                binding.rvForecast.adapter = WeatherForecastAdapter(nonLiveForecastWeather)
                binding.rvForecast.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

            val nonLiveHourlyForecast = viewModel.hourlyForecastNonLiveByLocationKey(it)
            if (!nonLiveHourlyForecast.isNullOrEmpty()) {
                binding.rv24hForecast.adapter = HourlyForecastAdapter(nonLiveHourlyForecast)
                binding.rv24hForecast.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
        binding.btn5DayForecast.setOnClickListener {
            val i = Intent(requireContext(), Detail5DayActivity::class.java)
            i.putExtra("locationKey", locationKey)
            startActivity(i)
        }
//        var url = "https://www.accuweather.com/en/vn/thanh-tri/${viewModel.key}/daily-weather-forecast/{}"


        bindUi()
    }

    fun convertAbbreviationToFullDirection(abbreviation: String): String {
        val directionMap = mapOf(
            "N" to "North",
            "NNE" to "North-Northeast",
            "NE" to "Northeast",
            "ENE" to "East-Northeast",
            "E" to "East",
            "ESE" to "East-Southeast",
            "SE" to "Southeast",
            "SSE" to "South-Southeast",
            "S" to "South",
            "SSW" to "South-Southwest",
            "SW" to "Southwest",
            "WSW" to "West-Southwest",
            "W" to "West",
            "WNW" to "West-Northwest",
            "NW" to "Northwest",
            "NNW" to "North-Northwest"
        )

        return directionMap[abbreviation] ?: abbreviation
    }

    // Method to set the scroll position for the fragment's ScrollView
    fun setScrollPosition(scrollX: Float) {
        binding.scrollView.scrollTo(scrollX.toInt(), 0)
    }
    private fun getRefresh(){
        lifecycleScope.launch {
            try {
                val currentweather = viewModel.currentWeatherByLocationKey.await()
                currentweather.observe(viewLifecycleOwner) {

                    if (it == null) return@observe

                    binding.tvTemp.text = Math.round(it.Temperature).toInt().toString()
                    binding.tvUnitDegree.text = "°${it.Unit}"
                    binding.weatherCondition.text = it.WeatherText
                    binding.textHumidity.text = "${it.RelativeHumidity}%"
                    binding.textRealFeel.text = "${it.RealFeelTemperature}°${it.Unit}"
                    binding.textUV.text = it.UVIndex.toString()
                    binding.textPressure.text = "${it.pressure}${it.pressureUnit}"
                    binding.tvWindDirCurrent.text = convertAbbreviationToFullDirection(it.windDirection)
                    binding.tvWindSpeedCurrent.text = "${it.windSpeed} ${it.windSpeedUnit}"
//                binding.textChanceOfRain.text = "${it.PrecipitationProbability}%"
                }

                val forecastweather = viewModel.forecastWeatherByLocationKey.await()
                forecastweather.observe(viewLifecycleOwner) {
                    if (it.isNullOrEmpty()) return@observe

                    binding.minmaxToday.text =
                        "${Math.round(it[0].minTemperature)}°/${Math.round(it[0].maxTemperature)}°"
                    binding.rvForecast.adapter = WeatherForecastAdapter(it)
                    binding.rvForecast.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                }
                val hourlyForecast = viewModel.hourlyForecastByLocationKey.await()
                hourlyForecast.observe(viewLifecycleOwner) {
                    if (it.isNullOrEmpty()) return@observe
                    binding.rv24hForecast.adapter = HourlyForecastAdapter(it)
                    binding.rv24hForecast.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
                if(currentweather.value != null && forecastweather.value != null && hourlyForecast.value != null){
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            } catch (e: NoConnectivityException) {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    private fun bindUi() = launch {
        try {
            val currentweather = viewModel.currentWeatherByLocationKey.await()
            currentweather.observe(viewLifecycleOwner) {

                if (it == null) return@observe

                binding.tvTemp.text = Math.round(it.Temperature).toInt().toString()
                binding.tvUnitDegree.text = "°${it.Unit}"
                binding.weatherCondition.text = it.WeatherText
                binding.textHumidity.text = "${it.RelativeHumidity}%"
                binding.textRealFeel.text = "${it.RealFeelTemperature}°${it.Unit}"
                binding.textUV.text = it.UVIndex.toString()
                binding.textPressure.text = "${it.pressure}${it.pressureUnit}"
                binding.tvWindDirCurrent.text = convertAbbreviationToFullDirection(it.windDirection)
                binding.tvWindSpeedCurrent.text = "${it.windSpeed} ${it.windSpeedUnit}"
//                binding.textChanceOfRain.text = "${it.PrecipitationProbability}%"
            }

            val forecastweather = viewModel.forecastWeatherByLocationKey.await()
            forecastweather.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe

                binding.minmaxToday.text =
                    "${Math.round(it[0].minTemperature)}°/${Math.round(it[0].maxTemperature)}°"
                binding.rvForecast.adapter = WeatherForecastAdapter(it)
                binding.rvForecast.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            val hourlyForecast = viewModel.hourlyForecastByLocationKey.await()
            hourlyForecast.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                binding.rv24hForecast.adapter = HourlyForecastAdapter(it)
                binding.rv24hForecast.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

        } catch (e: NoConnectivityException) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindU1() = launch {
        try {
            val currentweather = viewModel.currentWeatherByLocationKey.await()
            currentweather.observe(viewLifecycleOwner) {

                if (it == null) return@observe

                binding.tvTemp.text = Math.round(it.Temperature).toInt().toString()
                binding.tvUnitDegree.text = "°${it.Unit}"
                binding.weatherCondition.text = it.WeatherText
                binding.textHumidity.text = "${it.RelativeHumidity}%"
                binding.textRealFeel.text = "${it.RealFeelTemperature}°${it.Unit}"
                binding.textUV.text = it.UVIndex.toString()
                binding.textPressure.text = "${it.pressure}${it.pressureUnit}"
                binding.tvWindDirCurrent.text = convertAbbreviationToFullDirection(it.windDirection)
                binding.tvWindSpeedCurrent.text = "${it.windSpeed} ${it.windSpeedUnit}"
//                binding.textChanceOfRain.text = "${it.PrecipitationProbability}%"
            }

            val forecastweather = viewModel.forecastWeatherByLocationKey.await()
            forecastweather.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe

                binding.minmaxToday.text =
                    "${Math.round(it[0].minTemperature)}°/${Math.round(it[0].maxTemperature)}°"
                binding.rvForecast.adapter = WeatherForecastAdapter(it)
                binding.rvForecast.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            val hourlyForecast = viewModel.hourlyForecastByLocationKey.await()
            hourlyForecast.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                binding.swipeRefreshLayout.isRefreshing = false
                binding.rv24hForecast.adapter = HourlyForecastAdapter(it)
                binding.rv24hForecast.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

        } catch (e: NoConnectivityException) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }
}
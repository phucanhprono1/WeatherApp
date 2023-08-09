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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.HourlyForecastAdapter
import com.example.weatherapp.adapter.WeatherForecastAdapter

import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.provider.NoConnectivityException
import com.example.weatherapp.ui.Detail5DayActivity
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
        binding.rv24hForecast.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


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
        viewModel.key = arguments?.getString("locationKey")!!
        val nonLiveCurrentWeather = viewModel.currentWeatherNonLiveByLocationKey(arguments?.getString("locationKey")!!)
        binding.btn5DayForecast.setOnClickListener{
            startActivity(Intent(requireContext(), Detail5DayActivity::class.java))
        }


        if (nonLiveCurrentWeather != null) {
            binding.tvTemp.text = Math.round(nonLiveCurrentWeather.Temperature).toInt().toString()
            binding.tvUnitDegree.text = "°${nonLiveCurrentWeather.Unit}"
            binding.weatherCondition.text = nonLiveCurrentWeather.WeatherText
            binding.textHumidity.text = "${nonLiveCurrentWeather.RelativeHumidity}%"
            binding.textRealFeel.text = "${nonLiveCurrentWeather.RealFeelTemperature}°${nonLiveCurrentWeather.Unit}"
            binding.textUV.text = nonLiveCurrentWeather.UVIndex.toString()
            binding.textPressure.text = "${nonLiveCurrentWeather.pressure}${nonLiveCurrentWeather.pressureUnit}"
        }
        val nonLiveForecastWeather = viewModel.forecastWeatherNonLive
        if (nonLiveCurrentWeather!=null){
            binding.minmaxToday.text = "${Math.round(nonLiveForecastWeather[0].minTemperature)}°/${Math.round(nonLiveForecastWeather[0].maxTemperature)}°"
            binding.tvMoreDetails5dayForecast.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(nonLiveForecastWeather[0].link) ))
            }
            binding.rvForecast.adapter = WeatherForecastAdapter(nonLiveForecastWeather)
            binding.rvForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        val nonLiveHourlyForecast = viewModel.hourlyForecastNonLive
        if (nonLiveHourlyForecast!=null){
            binding.rv24hForecast.adapter = HourlyForecastAdapter(nonLiveHourlyForecast)
            binding.rv24hForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        bindUi()
    }


    // Method to set the scroll position for the fragment's ScrollView
    fun setScrollPosition(scrollX: Int) {
        binding.scrollView.scrollTo(scrollX, 0)
    }

    private fun bindUi()= launch {
        try{
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
//                binding.textChanceOfRain.text = "${it.PrecipitationProbability}%"
            }

            val forecastweather = viewModel.forecastWeather.await()
            forecastweather.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe

                binding.minmaxToday.text = "${Math.round(it[0].minTemperature)}°/${Math.round(it[0].maxTemperature)}°"
                binding.rvForecast.adapter = WeatherForecastAdapter(it)
                binding.rvForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            val hourlyForecast = viewModel.hourlyForecast.await()
            hourlyForecast.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                binding.rv24hForecast.adapter = HourlyForecastAdapter(it)
                binding.rv24hForecast.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

        }catch (e: NoConnectivityException) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

}
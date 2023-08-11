package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.FiveDayDetailAdapter
import com.example.weatherapp.databinding.ActivityDetail5DayBinding
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class Detail5DayActivity : AppCompatActivity() {
    @Inject
    lateinit var forecastRepository: ForecastRepository
    private lateinit var binding: ActivityDetail5DayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetail5DayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener{
            finish()
        }
        val fivedayForecast = forecastRepository.get5dayForecastNonLiveByLocationKey(LocalDateTime.now(),intent.getStringExtra("locationKey")!!,true)
        binding.rcvDetail5Day.adapter = FiveDayDetailAdapter(fivedayForecast)
        binding.rcvDetail5Day.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
}
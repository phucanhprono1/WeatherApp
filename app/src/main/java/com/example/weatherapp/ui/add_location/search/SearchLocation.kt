package com.example.weatherapp.ui.add_location.search

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.SearchResultAdapter
import com.example.weatherapp.databinding.ActivitySearchLocationBinding
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.repository.LocationRepositoryImpl
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class SearchLocation : AppCompatActivity(), SearchResultAdapter.OnResultClickListener {
    private var progressDialog: ProgressDialog? = null
    private lateinit var binding: ActivitySearchLocationBinding
    private var searchAdapter: SearchResultAdapter = SearchResultAdapter(this)
    val mainActivity = MainActivity()
    @Inject
    lateinit var locationRepo : LocationRepositoryImpl
    @Inject
    lateinit var forecastRepository: ForecastRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        prepareRecyclerView()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotBlank()) {
                        lifecycleScope.launch {
                            val locationList = locationRepo.searchCities(query)
                            if(locationList.isNullOrEmpty()){
                                Log.d("NULL NE", "NULL Ne")
                            }
                            else searchAdapter.setResultList(ArrayList(locationList))
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Vui long nhap ten thanh pho",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun prepareRecyclerView() {
        binding.searchResultRview.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = searchAdapter
        }
    }

    override fun onResultClick(location: LocationKeyResponse) {
        binding.progressBar.visibility = View.VISIBLE // Hiển thị ProgressBar

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                locationRepo.insertNewCity(location)
                val currentWeatherByKey = forecastRepository.getCurrentWeatherByLocationKey(location.Key, true)
                val dailyWeatherByKey = forecastRepository.getFutureWeatherListByLocationKey(
                    LocalDate.now(), location.Key, true)
                val hourlyWeatherByKey = forecastRepository.getHourlyForecastListByLocationKey(LocalDateTime.now(), location.Key, true)

                if (currentWeatherByKey != null && dailyWeatherByKey != null && hourlyWeatherByKey != null) {
                    // Tất cả dữ liệu đều có sẵn, tiến hành chèn vị trí vào cơ sở dữ liệu


                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE // Ẩn ProgressBar
                        finish()
                    }
                } else {
                    // Một hoặc nhiều nguồn dữ liệu không có sẵn, hiển thị thông báo cho người dùng
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE // Ẩn ProgressBar
                        Toast.makeText(applicationContext, "Không thể lấy đủ dữ liệu thời tiết", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE // Ẩn ProgressBar
                    Toast.makeText(applicationContext, "Lỗi khi gọi API", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
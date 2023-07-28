package com.example.weatherapp.ui.add_location.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.SearchResultAdapter
import com.example.weatherapp.databinding.ActivitySearchLocationBinding
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.repository.LocationRepositoryImpl
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchLocation : AppCompatActivity(), SearchResultAdapter.OnResultClickListener {

    private lateinit var binding: ActivitySearchLocationBinding
    private var searchAdapter: SearchResultAdapter = SearchResultAdapter(this)
    @Inject
    lateinit var locationRepo : LocationRepositoryImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                            searchAdapter.setResultList(ArrayList(locationList))
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

    override fun onResultClick(LocationKeyResponse: LocationKeyResponse) {

    }
}
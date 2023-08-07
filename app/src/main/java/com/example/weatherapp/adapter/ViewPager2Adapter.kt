package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.ui.fragment.currentweather.CurrentWeatherFragment

class ViewPager2Adapter(private val locationNames: List<String>) :
    RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentCurrentWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentCurrentWeatherBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationName = locationNames[position]
        // Bind data to your views in the layout
        // Example:
        // holder.binding.textView.text = locationName
    }

    override fun getItemCount(): Int {
        return locationNames.size
    }
}
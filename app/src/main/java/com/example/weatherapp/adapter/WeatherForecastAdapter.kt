package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast

class WeatherForecastAdapter(val fiveDaysForecast: FiveDaysForecast) : RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder>() {
    class WeatherForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val iconWeather = view.findViewById<ImageView>(R.id.ivWeatherConditionRcv)
        val tvDay = view.findViewById<TextView>(R.id.tvDayRcv)
        val weatherCondition= view.findViewById<TextView>(R.id.tvWeatherConditionRcv)
        val tvMinMaxTemp = view.findViewById<TextView>(R.id.tvMinMaxTempRcv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_forecast, parent, false)
        return WeatherForecastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        val forecast = fiveDaysForecast.DailyForecasts.get(position)
        holder.iconWeather.setImageResource(R.drawable.ic_cloud)
        holder.tvDay.text = forecast.Date
        holder.weatherCondition.text = forecast.Day.IconPhrase
        holder.tvMinMaxTemp.text = forecast.Temperature.Minimum.Value.toString() + "°C" + "/" + forecast.Temperature.Maximum.Value.toString() + "°C"
    }
}
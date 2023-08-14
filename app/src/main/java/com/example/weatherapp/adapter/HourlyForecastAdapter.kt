package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.response.forecast24h.Forecast24hItem
import com.example.weatherapp.weatherunit.forecast24h.UnitLocalized24hForecast
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class HourlyForecastAdapter(val list: List<UnitLocalized24hForecast>) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>(){
    class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val tvTime = view.findViewById<TextView>(R.id.tv_time_24h)
        val tvTemp = view.findViewById<TextView>(R.id.temperature_24h)
        val iconWeather = view.findViewById<ImageView>(R.id.iv_icon_weather_24h)
        val tvWind = view.findViewById<TextView>(R.id.tv_wind_24h)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_24h_forecast, parent, false)
        return HourlyForecastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val hourlyforecast = list.get(position)
        holder.tvTime.text = getTimeFromDateTime(hourlyforecast.DateTime, hourlyforecast.timezone)
        holder.tvTemp.text = Math.round(hourlyforecast.temperature_value).toInt().toString()+ "°"
        holder.tvWind.text = hourlyforecast.WindSpeed.toString() + "" + hourlyforecast.WindUnit
        when(hourlyforecast.WeatherIcon){
            1-> holder.iconWeather.setImageResource(R.drawable.sunny_01)
            2-> holder.iconWeather.setImageResource(R.drawable.mostly_sunny_02)
            3-> holder.iconWeather.setImageResource(R.drawable.partly_sunny_03)
            4-> holder.iconWeather.setImageResource(R.drawable.inter_cloud_04)
            5-> holder.iconWeather.setImageResource(R.drawable.hazy_sunshine_05)
            6-> holder.iconWeather.setImageResource(R.drawable.s_06)
            7-> holder.iconWeather.setImageResource(R.drawable.s07)
            8-> holder.iconWeather.setImageResource(R.drawable.ic_w08)
            11-> holder.iconWeather.setImageResource(R.drawable.ic_w11)
            12-> holder.iconWeather.setImageResource(R.drawable.ic_w12)
            13-> holder.iconWeather.setImageResource(R.drawable.ic_w13)
            14-> holder.iconWeather.setImageResource(R.drawable.ic_w14)
            15-> holder.iconWeather.setImageResource(R.drawable.ic_w15)
            16-> holder.iconWeather.setImageResource(R.drawable.ic_w16)
            17-> holder.iconWeather.setImageResource(R.drawable.ic_w17)
            18-> holder.iconWeather.setImageResource(R.drawable.ic_w18)
            19-> holder.iconWeather.setImageResource(R.drawable.ic_w19)
            20-> holder.iconWeather.setImageResource(R.drawable.ic_w20)
            21-> holder.iconWeather.setImageResource(R.drawable.ic_w21)
            22-> holder.iconWeather.setImageResource(R.drawable.ic_w22)
            23-> holder.iconWeather.setImageResource(R.drawable.ic_w23)
            24-> holder.iconWeather.setImageResource(R.drawable.ic_w24)
            25-> holder.iconWeather.setImageResource(R.drawable.ic_w25)
            26-> holder.iconWeather.setImageResource(R.drawable.ic_w26)
            29-> holder.iconWeather.setImageResource(R.drawable.ic_w29)
            30-> holder.iconWeather.setImageResource(R.drawable.ic_w30)
            31-> holder.iconWeather.setImageResource(R.drawable.ic_w31)
            32-> holder.iconWeather.setImageResource(R.drawable.ic_w32)
            33-> holder.iconWeather.setImageResource(R.drawable.ic_w33)
            34-> holder.iconWeather.setImageResource(R.drawable.ic_w34)
            35-> holder.iconWeather.setImageResource(R.drawable.ic_w35)
            36-> holder.iconWeather.setImageResource(R.drawable.ic_w36)
            37-> holder.iconWeather.setImageResource(R.drawable.ic_w37)
            38-> holder.iconWeather.setImageResource(R.drawable.ic_w38)
            39-> holder.iconWeather.setImageResource(R.drawable.ic_w39)
            41-> holder.iconWeather.setImageResource(R.drawable.ic_w41)
            42-> holder.iconWeather.setImageResource(R.drawable.ic_w42)
            43-> holder.iconWeather.setImageResource(R.drawable.ic_w43)
            44-> holder.iconWeather.setImageResource(R.drawable.ic_w44)
        }
    }
    fun getTimeFromDateTime(dateTimeString: String, timeZone: String): String {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            val zonedDateTime = ZonedDateTime.parse(dateTimeString, formatter)

            val targetZone = ZoneId.of(timeZone)
            val convertedDateTime = zonedDateTime.withZoneSameInstant(targetZone)

            val hour = convertedDateTime.hour
            val minute = convertedDateTime.minute

            return String.format("%02d:%02d", hour, minute)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}
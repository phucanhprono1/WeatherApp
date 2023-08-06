package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle

import java.time.temporal.ChronoUnit
import java.util.Locale

class WeatherForecastAdapter(val list: List<UnitLocalizedFiveDaysForecastWeather>) : RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder>() {
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
        if (list.size ==0) {
            return 0
        }
        else
            return 3
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        val forecast = list.get(position)
        when(forecast.icon_day){
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

        holder.tvDay.text = formatDateToDayOfWeek(forecast.date)
        holder.weatherCondition.text = forecast.iconPhrase_day
        holder.tvMinMaxTemp.text = Math.round(forecast.minTemperature).toInt().toString() + "°" + "/" + Math.round(forecast.maxTemperature).toInt().toString() + "°"
    }
    fun formatDateToDayOfWeek(inputDateStr: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val inputDate = ZonedDateTime.parse(inputDateStr, formatter)

        // Lấy ngày hôm nay
        val today = LocalDate.now()

        // Lấy ngày tiếp theo (ngày mai)
        val tomorrow = today.plusDays(1)

        // Lấy ngày trong tuần
        val dayOfWeek = inputDate.toLocalDate().dayOfWeek

        return when {
            inputDate.toLocalDate() == today -> "Today"
            inputDate.toLocalDate() == tomorrow -> "Tomorrow"
            else -> dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).capitalize()
        }
    }
}
package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Locale


class FiveDayDetailAdapter(val list: List<UnitLocalizedFiveDaysForecastWeather>) : RecyclerView.Adapter<FiveDayDetailAdapter.WeatherForecastViewHolder>() {

    class WeatherForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDayWeek: TextView = itemView.findViewById(R.id.dayweek)
        val tvDate: TextView = itemView.findViewById(R.id.date_forecast)
        val ivIconDay: ImageView = itemView.findViewById(R.id.iv_icon_weather_5_day_day)
        val tvMaxTemp: TextView = itemView.findViewById(R.id.temperature_5_day_max)
        val tvMinTemp: TextView = itemView.findViewById(R.id.temperature_5_day_min)
        val ivIconNight: ImageView = itemView.findViewById(R.id.iv_icon_weather_5_day_night)
        val tvWind: TextView = itemView.findViewById(R.id.tv_wind_24h)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_5_day_detail, parent, false)
        return WeatherForecastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        val forecast = list[position]

        holder.tvDayWeek.text = formatDateToDayOfWeek(forecast.date,forecast.timezone)
        holder.tvDate.text = formatDate(forecast.date, forecast.timezone)
        holder.ivIconDay.setImageResource(getWeatherIconResource(forecast.icon_day))
        holder.tvMaxTemp.text = "${Math.round(forecast.maxTemperature)}°"
        holder.tvMinTemp.text = "${Math.round(forecast.minTemperature)}°"
        holder.ivIconNight.setImageResource(getWeatherIconResource(forecast.icon_night))
        holder.tvWind.text = "${forecast.WindSpeed}${forecast.WindUnit}"
    }

    private fun formatDateToDayOfWeek(inputDateStr: String, timeZone: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val inputDate = ZonedDateTime.parse(inputDateStr, formatter)

        val targetZone = ZoneId.of(timeZone)
        val deviceZone = ZoneId.systemDefault()

        val convertedDateTime = inputDate.withZoneSameInstant(targetZone)
        val today = ZonedDateTime.now(targetZone).toLocalDate()
        val tomorrow = today.plusDays(1)
        val dayOfWeek = convertedDateTime.toLocalDate().dayOfWeek

        return when {
            targetZone == deviceZone && convertedDateTime.toLocalDate() == today -> "Today"
            targetZone == deviceZone && convertedDateTime.toLocalDate() == tomorrow -> "Tomorrow"
            else -> dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).capitalize()
        }
    }
    private fun formatDate(inputDateStr: String, timeZone: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val inputDate = ZonedDateTime.parse(inputDateStr, formatter)

        val targetZone = ZoneId.of(timeZone)
        val convertedDateTime = inputDate.withZoneSameInstant(targetZone)

        val formattedDate = convertedDateTime.format(DateTimeFormatter.ofPattern("d/M"))
        return formattedDate
    }

    private fun getWeatherIconResource(iconCode: Int): Int {
        return when (iconCode) {
            1 -> R.drawable.sunny_01
            2 -> R.drawable.mostly_sunny_02
            3 -> R.drawable.partly_sunny_03
            4 -> R.drawable.inter_cloud_04
            5 -> R.drawable.hazy_sunshine_05
            6 -> R.drawable.s_06
            7 -> R.drawable.s07
            8 -> R.drawable.ic_w08
            11 -> R.drawable.ic_w11
            12 -> R.drawable.ic_w12
            13 -> R.drawable.ic_w13
            14 -> R.drawable.ic_w14
            15 -> R.drawable.ic_w15
            16 -> R.drawable.ic_w16
            17 -> R.drawable.ic_w17
            18 -> R.drawable.ic_w18
            19 -> R.drawable.ic_w19
            20 -> R.drawable.ic_w20
            21 -> R.drawable.ic_w21
            22 -> R.drawable.ic_w22
            23 -> R.drawable.ic_w23
            24 -> R.drawable.ic_w24
            25 -> R.drawable.ic_w25
            26 -> R.drawable.ic_w26
            29 -> R.drawable.ic_w29
            30 -> R.drawable.ic_w30
            31 -> R.drawable.ic_w31
            32 -> R.drawable.ic_w32
            33 -> R.drawable.ic_w33
            34 -> R.drawable.ic_w34
            35 -> R.drawable.ic_w35
            36 -> R.drawable.ic_w36
            37 -> R.drawable.ic_w37
            38 -> R.drawable.ic_w38
            39 -> R.drawable.ic_w39
            41 -> R.drawable.ic_w41
            42 -> R.drawable.ic_w42
            43 -> R.drawable.ic_w43
            44 -> R.drawable.ic_w44
            else -> R.drawable.ic_cloud // You can use a default drawable for unknown icons
        }
    }
}
package com.example.weatherapp.room

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun stringToDateTime(str: String?): Any? {
        if (str != null) {
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (ex: Exception) {
                // Parsing as LocalDateTime failed, try parsing as LocalDate
                try {
                    return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (ex: Exception) {
                    throw IllegalArgumentException("Invalid date/time format: $str")
                }
            }
        }
        return null
    }

    @TypeConverter
    @JvmStatic
    fun dateTimeToString(dateTime: Any?): String? {
        return dateTime?.let {
            when (it) {
                is LocalDateTime -> it.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                is LocalDate -> it.format(DateTimeFormatter.ISO_LOCAL_DATE)
                else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
            }
        }
    }
}
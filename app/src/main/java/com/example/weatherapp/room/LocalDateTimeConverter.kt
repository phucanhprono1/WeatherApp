package com.example.weatherapp.room

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object LocalDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun stringToDateTime(str: String?) = str?.let {
        LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
    @TypeConverter
    @JvmStatic
    fun dateTimeToString(dateTime: LocalDateTime?) = dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    @TypeConverter
    @JvmStatic
    fun zonedSDateTimeToString(dateTime: ZonedDateTime?) = dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    @TypeConverter
    @JvmStatic
    fun DateToString(dateTime: LocalDate?) = dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE)
}
package com.example.karam.data

import androidx.room.TypeConverter
import com.example.karam.util.DateUtils
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPersianDateString(value: String?): Date? {
        return value?.let {
            val parts = it.split("/")
            if (parts.size == 3) {
                val persianDate = com.github.samanzamani.persiandate.PersianDate()
                persianDate.year = parts[0].toInt()
                persianDate.month = parts[1].toInt()
                persianDate.day = parts[2].toInt()
                persianDate.toDate()
            } else null
        }
    }

    @TypeConverter
    fun toPersianDateString(date: Date?): String? {
        return date?.let { DateUtils.getPersianDateString(it) }
    }
} 
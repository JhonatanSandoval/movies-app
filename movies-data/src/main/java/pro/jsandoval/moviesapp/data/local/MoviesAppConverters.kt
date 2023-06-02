package pro.jsandoval.moviesapp.data.local

import androidx.room.TypeConverter
import java.util.Date

class MoviesAppConverters {

    @TypeConverter
    fun toDate(millis: Long): Date {
        return Date(millis)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}
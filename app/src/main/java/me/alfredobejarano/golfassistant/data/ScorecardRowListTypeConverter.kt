package me.alfredobejarano.golfassistant.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder

class ScorecardRowListTypeConverter {
    companion object {
        private val gson by lazy { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

        @JvmStatic
        @TypeConverter
        fun convertToStringList(rows: List<ScorecardRow>) = gson.run { rows.map(::toJson) }

        @JvmStatic
        @TypeConverter
        fun convertToRowList(rows: List<String>): List<ScorecardRow> = gson.run { rows.map(::fromJson) }
    }
}
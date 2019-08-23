package me.alfredobejarano.golfassistant.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ScorecardRowListTypeConverter {
    companion object {
        private val listType = object : TypeToken<List<ScorecardRow>>() {}.type
        private val gson by lazy { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

        @JvmStatic
        @TypeConverter
        fun convertToStringList(rows: List<ScorecardRow>): String = gson.toJson(rows)

        @JvmStatic
        @TypeConverter
        fun convertToRowList(rows: String): List<ScorecardRow> = gson.fromJson(rows, listType)
    }
}
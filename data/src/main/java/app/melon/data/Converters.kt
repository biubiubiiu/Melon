package app.melon.data

import androidx.room.TypeConverter
import app.melon.data.entities.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sun.rmi.runtime.Log

object Converters {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromString(listOfString: String): List<String> {
        return gson.fromJson(listOfString, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    @JvmStatic
    fun fromList(listOfString: List<String>): String {
        return gson.toJson(listOfString)
    }

    @TypeConverter
    @JvmStatic
    fun location2String(location: Location): String {
        return location.toString()
    }

    @TypeConverter
    @JvmStatic
    fun string2Location(str: String): Location {
        val data = str.trimStart('(').trimEnd(')').split(',', limit = 2)
        return Location(
            longitude = data[0].toDouble(),
            latitude = data[1].toDouble()
        )
    }
}
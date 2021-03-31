package app.melon.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
}
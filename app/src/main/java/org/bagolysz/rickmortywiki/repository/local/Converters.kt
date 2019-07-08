package org.bagolysz.rickmortywiki.repository.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.bagolysz.rickmortywiki.model.data.Location
import org.bagolysz.rickmortywiki.model.data.Origin

class Converters {

    @TypeConverter
    fun fromOriginToString(value: Origin): String {
        val gson = Gson()
        val json = gson.toJson(value)
        return json
    }

    @TypeConverter
    fun fromStringToOrigin(value: String): Origin {
        val gson = Gson()
        val origin = gson.fromJson(value, Origin::class.java)
        return origin
    }

    @TypeConverter
    fun fromLocationToString(value: Location): String {
        val gson = Gson()
        val json = gson.toJson(value)
        return json
    }

    @TypeConverter
    fun fromStringToLocation(value: String): Location {
        val gson = Gson()
        val location = gson.fromJson(value, Location::class.java)
        return location
    }

    @TypeConverter
    fun fromListToString(value: List<String>): String {
        val gson = Gson()
        val json = gson.toJson(value)
        return json
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        val gson = Gson()
        val array = gson.fromJson(value, Array<String>::class.java)
        val list = mutableListOf<String>()
        for (i in array) {
            list.add(i)
        }
        return list
    }
}
package com.devphill.cocktails.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",") { "\"$it\"" }.let { "[$it]" }
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            value.removeSurrounding("[", "]")
                .split(",")
                .map { it.trim().removeSurrounding("\"") }
                .filter { it.isNotEmpty() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

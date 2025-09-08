package com.devphill.cocktails.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<CocktailDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/cocktail_database.db"
    return Room.databaseBuilder<CocktailDatabase>(
        name = dbFilePath,
    )
}

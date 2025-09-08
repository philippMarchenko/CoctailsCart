package com.devphill.cocktails.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<CocktailDatabase> {
    val appContext = context as Context
    val dbFile = appContext.getDatabasePath("cocktail_database")
    return Room.databaseBuilder<CocktailDatabase>(
        context = appContext.applicationContext,
        name = dbFile.absolutePath,
    ).fallbackToDestructiveMigration(dropAllTables = true) // This will recreate the database if migration is needed
}

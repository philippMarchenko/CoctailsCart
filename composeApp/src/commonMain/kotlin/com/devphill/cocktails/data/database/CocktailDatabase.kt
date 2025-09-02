package com.devphill.cocktails.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devphill.cocktails.data.database.dao.CocktailDao
import com.devphill.cocktails.data.database.entity.CocktailEntity

@Database(
    entities = [CocktailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao
}

// Platform-specific database builder function
expect fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<CocktailDatabase>

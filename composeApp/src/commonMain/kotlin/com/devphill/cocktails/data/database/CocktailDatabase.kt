package com.devphill.cocktails.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devphill.cocktails.data.database.dao.CocktailDao
import com.devphill.cocktails.data.database.dao.NotificationDao
import com.devphill.cocktails.data.database.entity.CocktailEntity
import com.devphill.cocktails.data.database.entity.NotificationEntity

@Database(
    entities = [CocktailEntity::class, NotificationEntity::class],
    version = 2, // Increment version for database migration
    exportSchema = false
)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao
    abstract fun notificationDao(): NotificationDao
}

// Platform-specific database builder function
expect fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<CocktailDatabase>

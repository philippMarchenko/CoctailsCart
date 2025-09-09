package com.devphill.cocktails.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val type: String, // Store as string to avoid enum serialization issues
    val timestamp: Long, // Store as Long for easier sorting
    val isRead: Boolean = false,
    val cocktailId: String? = null,
    val actionUrl: String? = null,
)

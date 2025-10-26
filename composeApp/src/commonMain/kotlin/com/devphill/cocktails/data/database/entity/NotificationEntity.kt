package com.devphill.cocktails.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val type: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val cocktailId: String? = null,
    val actionUrl: String? = null,
)

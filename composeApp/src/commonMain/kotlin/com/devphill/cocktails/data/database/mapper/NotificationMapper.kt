package com.devphill.cocktails.data.database.mapper

import com.devphill.cocktails.data.database.entity.NotificationEntity
import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType

/**
 * Mapper functions to convert between domain models and database entities for notifications
 */

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        title = title,
        message = message,
        type = NotificationType.valueOf(type),
        timestamp = timestamp.toString(),
        isRead = isRead,
        cocktailId = cocktailId,
        actionUrl = actionUrl
    )
}

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        type = type.name,
        timestamp = timestamp.toLongOrNull() ?: System.currentTimeMillis(),
        isRead = isRead,
        cocktailId = cocktailId,
        actionUrl = actionUrl
    )
}

fun List<NotificationEntity>.toDomain(): List<Notification> {
    return map { it.toDomain() }
}

fun List<Notification>.toEntity(): List<NotificationEntity> {
    return map { it.toEntity() }
}

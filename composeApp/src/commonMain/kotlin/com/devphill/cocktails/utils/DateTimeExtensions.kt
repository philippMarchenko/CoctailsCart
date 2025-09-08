package com.devphill.cocktails.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Utility extension functions for date and time formatting.
 */
fun String.formatToEuropeanDateTime(): String {
    return try {
        // Parse Unix timestamp (milliseconds since epoch)
        val timestampLong = this.toLongOrNull()

        if (timestampLong != null) {
            // Convert to kotlinx-datetime Instant
            val instant = Instant.fromEpochMilliseconds(timestampLong)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            // Format as European date and time with zero padding
            val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
            val month = localDateTime.monthNumber.toString().padStart(2, '0')
            val year = localDateTime.year.toString()
            val hour = localDateTime.hour.toString().padStart(2, '0')
            val minute = localDateTime.minute.toString().padStart(2, '0')

            // Return European format: DD.MM.YYYY, HH:MM
            "$day.$month.$year, $hour:$minute"
        } else {
            // Fallback: try to parse as date string format "2024-08-27 10:30:00"
            val parts = this.split(" ")
            val datePart = parts[0] // "2024-08-27"
            val timePart = if (parts.size > 1) parts[1] else null // "10:30:00"

            // Split date into components
            val dateComponents = datePart.split("-")
            if (dateComponents.size == 3) {
                val year = dateComponents[0]
                val month = dateComponents[1]
                val day = dateComponents[2]

                // Format as European date (DD.MM.YYYY)
                val europeanDate = "$day.$month.$year"

                // Add time if available, format as HH:MM
                return if (timePart != null && timePart.isNotEmpty()) {
                    val timeComponents = timePart.split(":")
                    if (timeComponents.size >= 2) {
                        val hour = timeComponents[0]
                        val minute = timeComponents[1]
                        "$europeanDate, $hour:$minute"
                    } else {
                        europeanDate
                    }
                } else {
                    europeanDate
                }
            } else {
                // Final fallback
                this
            }
        }
    } catch (_: Exception) {
        // Fallback to original format if any error occurs
        this
    }
}

/**
 * Formats a Unix timestamp string to European date format only (DD.MM.YYYY).
 *
 * @param timestamp Unix timestamp as string (milliseconds) or date string format
 * @return Formatted date string in European format "DD.MM.YYYY"
 */
fun String.formatToEuropeanDate(): String {
    return try {
        val timestampLong = this.toLongOrNull()

        if (timestampLong != null) {
            val instant = Instant.fromEpochMilliseconds(timestampLong)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
            val month = localDateTime.monthNumber.toString().padStart(2, '0')
            val year = localDateTime.year.toString()

            "$day.$month.$year"
        } else {
            // Fallback for string format
            val datePart = this.split(" ")[0]
            val dateComponents = datePart.split("-")
            if (dateComponents.size == 3) {
                val year = dateComponents[0]
                val month = dateComponents[1]
                val day = dateComponents[2]
                "$day.$month.$year"
            } else {
                this
            }
        }
    } catch (_: Exception) {
        this
    }
}

/**
 * Formats a Unix timestamp string to time format only (HH:MM).
 *
 * @param timestamp Unix timestamp as string (milliseconds) or date string format
 * @return Formatted time string "HH:MM"
 */
fun String.formatToTime(): String {
    return try {
        val timestampLong = this.toLongOrNull()

        if (timestampLong != null) {
            val instant = Instant.fromEpochMilliseconds(timestampLong)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            val hour = localDateTime.hour.toString().padStart(2, '0')
            val minute = localDateTime.minute.toString().padStart(2, '0')

            "$hour:$minute"
        } else {
            // Fallback for string format
            val parts = this.split(" ")
            if (parts.size > 1) {
                val timeComponents = parts[1].split(":")
                if (timeComponents.size >= 2) {
                    val hour = timeComponents[0]
                    val minute = timeComponents[1]
                    "$hour:$minute"
                } else {
                    "00:00"
                }
            } else {
                "00:00"
            }
        }
    } catch (_: Exception) {
        "00:00"
    }
}

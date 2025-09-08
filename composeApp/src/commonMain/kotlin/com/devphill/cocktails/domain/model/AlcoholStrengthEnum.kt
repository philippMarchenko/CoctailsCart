package com.devphill.cocktails.domain.model

/**
 * Represents the alcohol strength classification of a cocktail.
 * Used to categorize cocktails based on their alcohol content level.
 *
 * @property key The unique identifier for the alcohol strength level
 * @property value The human-readable display name of the alcohol strength level
 */
data class AlcoholStrengthEnum(
    val key: String,
    val value: String,
)

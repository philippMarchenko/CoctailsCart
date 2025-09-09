package com.devphill.cocktails.domain.model

/**
 * Represents a cocktail category with a key-value pair structure.
 * Used for categorizing cocktails into different types or styles.
 *
 * @property key The unique identifier for the category
 * @property value The human-readable display name of the category
 */
data class CategoryEnum(
    val key: String,
    val value: String,
)

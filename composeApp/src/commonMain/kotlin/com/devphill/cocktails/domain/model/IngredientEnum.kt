package com.devphill.cocktails.domain.model

/**
 * Represents an individual ingredient with a key-value pair structure.
 * Used to define ingredients that can be used in cocktail recipes.
 *
 * @property key The unique identifier for the ingredient
 * @property value The human-readable display name of the ingredient
 */
data class IngredientEnum(
    val key: String,
    val value: String
)

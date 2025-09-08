package com.devphill.cocktails.domain.model

/**
 * Represents the complexity level of a cocktail preparation.
 * Used to classify cocktails based on how difficult they are to make.
 *
 * @property key The unique identifier for the complexity level
 * @property value The human-readable display name of the complexity level
 */
data class ComplexityEnum(
    val key: String,
    val value: String,
)

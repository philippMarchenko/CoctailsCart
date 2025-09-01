package com.devphill.cocktails.domain.model

/**
 * Represents a complete cocktail entity with all necessary information
 * for display, preparation, and user interaction.
 *
 * @property id Unique identifier for the cocktail
 * @property title The name of the cocktail
 * @property imageUrl Optional URL to the cocktail's image
 * @property cocktailUrl Optional URL to the cocktail's detailed page
 * @property category String representation of the cocktail category
 * @property categoryEnum String representation of category enum
 * @property views Optional view count as string
 * @property ingredients List of ingredient names used in the cocktail
 * @property ingredientsEnums List of ingredient enum representations
 * @property method Preparation instructions for the cocktail
 * @property garnish Optional garnish information
 * @property glass Optional glass type recommendation
 * @property videoUrl Optional URL to preparation video
 * @property complexity Complexity level enum for the cocktail preparation
 * @property alcoholStrength Alcohol strength classification enum
 * @property searchText Searchable text content for this cocktail
 * @property isFavorite Whether this cocktail is marked as favorite by the user
 * @property preparationTime Estimated preparation time in minutes (auto-calculated)
 */
data class Cocktail(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val cocktailUrl: String?,
    val category: String,
    val categoryEnum: String,
    val views: String?,
    val ingredients: List<String>,
    val ingredientsEnums: List<String>,
    val method: String,
    val garnish: String?,
    val glass: String?,
    val videoUrl: String?,
    val complexity: ComplexityLevel,
    val alcoholStrength: AlcoholStrength,
    val searchText: String,
    val isFavorite: Boolean = false,
    val preparationTime: Int = estimatePreparationTime(method, ingredients.size)
)

/**
 * Enumeration representing the complexity level of cocktail preparation.
 * Used to help users choose cocktails based on their skill level.
 */
enum class ComplexityLevel {
    /** Simple cocktails requiring basic mixing techniques */
    SIMPLE,
    /** Medium complexity cocktails with moderate preparation steps */
    MEDIUM,
    /** Complex cocktails requiring advanced techniques and multiple steps */
    COMPLEX;

    companion object {
        /**
         * Converts a string value to ComplexityLevel enum.
         * @param value String representation of complexity level
         * @return Corresponding ComplexityLevel enum, defaults to MEDIUM if unknown
         */
        fun fromString(value: String): ComplexityLevel = when (value.lowercase()) {
            "simple" -> SIMPLE
            "medium" -> MEDIUM
            "complex" -> COMPLEX
            else -> MEDIUM
        }
    }
}

/**
 * Enumeration representing the alcohol strength classification of cocktails.
 * Helps users choose cocktails based on desired alcohol content.
 */
enum class AlcoholStrength {
    /** Cocktails with no alcohol content */
    NON_ALCOHOLIC,
    /** Cocktails with light alcohol content */
    LIGHT,
    /** Cocktails with medium alcohol content */
    MEDIUM,
    /** Cocktails with strong alcohol content */
    STRONG;

    companion object {
        /**
         * Converts a string value to AlcoholStrength enum.
         * @param value String representation of alcohol strength
         * @return Corresponding AlcoholStrength enum, defaults to MEDIUM if unknown
         */
        fun fromString(value: String): AlcoholStrength = when (value.lowercase()) {
            "non_alcoholic", "non-alcoholic" -> NON_ALCOHOLIC
            "light" -> LIGHT
            "medium" -> MEDIUM
            "strong" -> STRONG
            else -> MEDIUM
        }
    }
}

/**
 * Estimates the preparation time for a cocktail based on its method and ingredient count.
 * Takes into account different preparation techniques and ingredient complexity.
 *
 * @param method The preparation method description
 * @param ingredientCount Number of ingredients in the cocktail
 * @return Estimated preparation time in minutes
 */
private fun estimatePreparationTime(method: String, ingredientCount: Int): Int {
    val baseTime = when {
        method.contains("shake", true) -> 3
        method.contains("stir", true) -> 2
        method.contains("muddle", true) -> 4
        method.contains("blend", true) -> 5
        else -> 2
    }
    return baseTime + (ingredientCount * 0.5).toInt()
}

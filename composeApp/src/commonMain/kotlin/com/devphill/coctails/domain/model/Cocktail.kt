package com.devphill.coctails.domain.model

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
    val rating: Float = 0f,
    val isFavorite: Boolean = false,
    val preparationTime: Int = estimatePreparationTime(method, ingredients.size)
)

enum class ComplexityLevel {
    SIMPLE, MEDIUM, COMPLEX;

    companion object {
        fun fromString(value: String): ComplexityLevel = when (value.lowercase()) {
            "simple" -> SIMPLE
            "medium" -> MEDIUM
            "complex" -> COMPLEX
            else -> MEDIUM
        }
    }
}

enum class AlcoholStrength {
    NON_ALCOHOLIC, LIGHT, MEDIUM, STRONG;

    companion object {
        fun fromString(value: String): AlcoholStrength = when (value.lowercase()) {
            "non_alcoholic", "non-alcoholic" -> NON_ALCOHOLIC
            "light" -> LIGHT
            "medium" -> MEDIUM
            "strong" -> STRONG
            else -> MEDIUM
        }
    }
}

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

package com.devphill.cocktails.domain.model

/**
 * Container structure that holds all ingredient-related data.
 * Provides both a complete list of ingredients and categorized access.
 *
 * @property allIngredients Complete list of all available ingredients
 * @property byCategory Ingredients organized by category for filtered access
 */
data class IngredientsStructure(
    val allIngredients: List<IngredientEnum>,
    val byCategory: IngredientsByCategory,
)

package com.devphill.cocktails.domain.model

/**
 * Organizes ingredients by their categories for easy filtering and navigation.
 * Groups ingredients into logical categories commonly used in cocktail preparation.
 *
 * @property spirits List of spirit-based ingredients (whiskey, vodka, etc.)
 * @property liqueurs List of liqueur ingredients (triple sec, amaretto, etc.)
 * @property mixers List of mixer ingredients (soda, tonic, etc.)
 * @property juices List of juice ingredients (lime, lemon, etc.)
 * @property bitters List of bitter ingredients (angostura, orange, etc.)
 * @property syrups List of syrup ingredients (simple syrup, grenadine, etc.)
 * @property other List of miscellaneous ingredients that don't fit other categories
 */
data class IngredientsByCategory(
    val spirits: List<IngredientEnum>,
    val liqueurs: List<IngredientEnum>,
    val mixers: List<IngredientEnum>,
    val juices: List<IngredientEnum>,
    val bitters: List<IngredientEnum>,
    val syrups: List<IngredientEnum>,
    val other: List<IngredientEnum>
)

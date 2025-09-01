package com.devphill.cocktails.domain.model

/**
 * Main data container that holds all cocktail-related information including
 * cocktails, categories, ingredients, complexity levels, and alcohol strengths.
 *
 * @property cocktails List of all available cocktail details
 * @property categories List of available cocktail categories
 * @property ingredients Structure containing all ingredient information
 * @property complexityLevels List of complexity level options
 * @property alcoholStrengths List of alcohol strength options
 */
data class CocktailsData(
    val cocktails: List<Cocktail>,
    val categories: List<CategoryEnum>,
    val ingredients: IngredientsStructure,
    val complexityLevels: List<ComplexityEnum>,
    val alcoholStrengths: List<AlcoholStrengthEnum>
)

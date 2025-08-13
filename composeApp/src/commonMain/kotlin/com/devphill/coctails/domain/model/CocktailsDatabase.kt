package com.devphill.coctails.domain.model

data class CocktailsDatabase(
    val cocktails: List<CocktailDetail>,
    val categories: List<CategoryEnum>,
    val ingredients: IngredientsStructure,
    val complexityLevels: List<ComplexityEnum>,
    val alcoholStrengths: List<AlcoholStrengthEnum>
)

data class CocktailDetail(
    val title: String,
    val imageUrl: String?,
    val cocktailUrl: String?,
    val category: String,
    val views: String?,
    val ingredients: List<String>,
    val method: String,
    val garnish: String?,
    val glass: String?,
    val videoUrl: String?,
    val categoryEnum: String,
    val ingredientsEnums: List<String>,
    val complexity: String,
    val alcoholStrength: String,
    val searchText: String
)

data class CategoryEnum(
    val key: String,
    val value: String
)

data class IngredientsStructure(
    val allIngredients: List<IngredientEnum>,
    val byCategory: IngredientsByCategory
)

data class IngredientEnum(
    val key: String,
    val value: String
)

data class IngredientsByCategory(
    val spirits: List<IngredientEnum>,
    val liqueurs: List<IngredientEnum>,
    val mixers: List<IngredientEnum>,
    val juices: List<IngredientEnum>,
    val bitters: List<IngredientEnum>,
    val syrups: List<IngredientEnum>,
    val other: List<IngredientEnum>
)

data class ComplexityEnum(
    val key: String,
    val value: String
)

data class AlcoholStrengthEnum(
    val key: String,
    val value: String
)

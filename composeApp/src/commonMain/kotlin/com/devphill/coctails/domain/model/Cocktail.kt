package com.devphill.coctails.domain.model

data class Cocktail(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: String,
    val category: String,
    val isAlcoholic: Boolean,
    val difficulty: DifficultyLevel,
    val preparationTime: Int, // in minutes
    val rating: Float
)

enum class DifficultyLevel {
    EASY, MEDIUM, HARD
}

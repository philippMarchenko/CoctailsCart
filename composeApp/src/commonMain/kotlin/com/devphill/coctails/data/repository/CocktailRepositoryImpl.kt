package com.devphill.coctails.data.repository

import com.devphill.coctails.domain.model.Cocktail
import com.devphill.coctails.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CocktailRepositoryImpl : CocktailRepository {

    // Mock data for demonstration - replace with actual data source later
    private val mockCocktails = listOf(
        Cocktail(
            id = "1",
            name = "Mojito",
            description = "A refreshing Cuban cocktail with mint and lime",
            imageUrl = "",
            ingredients = listOf("White rum", "Fresh lime juice", "Sugar", "Mint leaves", "Soda water"),
            instructions = "Muddle mint leaves with sugar and lime juice. Add rum and ice, top with soda water.",
            category = "Refreshing",
            isAlcoholic = true,
            difficulty = com.devphill.coctails.domain.model.DifficultyLevel.EASY,
            preparationTime = 5,
            rating = 4.5f
        ),
        Cocktail(
            id = "2",
            name = "Virgin Mojito",
            description = "Non-alcoholic version of the classic mojito",
            imageUrl = "",
            ingredients = listOf("Fresh lime juice", "Sugar", "Mint leaves", "Soda water"),
            instructions = "Muddle mint leaves with sugar and lime juice. Add ice and top with soda water.",
            category = "Non-alcoholic",
            isAlcoholic = false,
            difficulty = com.devphill.coctails.domain.model.DifficultyLevel.EASY,
            preparationTime = 3,
            rating = 4.2f
        )
    )

    private val favorites = mutableSetOf<String>()

    override suspend fun getAllCocktails(): Flow<List<Cocktail>> = flow {
        emit(mockCocktails)
    }

    override suspend fun getCocktailById(id: String): Cocktail? {
        return mockCocktails.find { it.id == id }
    }

    override suspend fun searchCocktails(query: String): Flow<List<Cocktail>> = flow {
        val filtered = mockCocktails.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.ingredients.any { ingredient -> ingredient.contains(query, ignoreCase = true) }
        }
        emit(filtered)
    }

    override suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> = flow {
        val favoriteCocktails = mockCocktails.filter { it.id in favorites }
        emit(favoriteCocktails)
    }

    override suspend fun addToFavorites(cocktail: Cocktail) {
        favorites.add(cocktail.id)
    }

    override suspend fun removeFromFavorites(cocktailId: String) {
        favorites.remove(cocktailId)
    }

    override suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> = flow {
        val filtered = mockCocktails.filter { it.category == category }
        emit(filtered)
    }
}

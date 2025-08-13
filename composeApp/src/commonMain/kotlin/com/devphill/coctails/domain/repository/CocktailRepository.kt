package com.devphill.coctails.domain.repository

import com.devphill.coctails.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    suspend fun getAllCocktails(): Flow<List<Cocktail>>
    suspend fun getCocktailById(id: String): Cocktail?
    suspend fun searchCocktails(query: String): Flow<List<Cocktail>>
    suspend fun getFavoriteCocktails(): Flow<List<Cocktail>>
    suspend fun addToFavorites(cocktail: Cocktail)
    suspend fun removeFromFavorites(cocktailId: String)
    suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>>
}

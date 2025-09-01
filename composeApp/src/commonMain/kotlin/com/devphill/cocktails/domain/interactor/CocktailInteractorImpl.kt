package com.devphill.cocktails.domain.interactor

import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow

/**
 * Default implementation of CocktailInteractor.
 *
 * @param repository The cocktail repository for data access
 */
class CocktailInteractorImpl(
    private val repository: CocktailRepository
) : CocktailInteractor {

    override suspend fun getAllCocktails(): Flow<List<Cocktail>> {
        return repository.getAllCocktails()
    }

    override suspend fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return repository.searchCocktails(query)
    }

    override suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> {
        return repository.getFavoriteCocktails()
    }

    override suspend fun getCocktailById(id: String): Cocktail? {
        return repository.getCocktailById(id)
    }

    override suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> {
        return repository.getCocktailsByCategory(category)
    }

    override suspend fun toggleFavorite(cocktail: Cocktail, isFavorite: Boolean) {
        if (isFavorite) {
            repository.removeFromFavorites(cocktail.id)
        } else {
            repository.addToFavorites(cocktail)
        }
    }
}

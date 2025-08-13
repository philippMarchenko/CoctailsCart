package com.devphill.coctails.domain.usecase

import com.devphill.coctails.domain.model.Cocktail
import com.devphill.coctails.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow

class GetAllCocktailsUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(): Flow<List<Cocktail>> {
        return repository.getAllCocktails()
    }
}

class SearchCocktailsUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Cocktail>> {
        return repository.searchCocktails(query)
    }
}

class GetFavoriteCocktailsUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(): Flow<List<Cocktail>> {
        return repository.getFavoriteCocktails()
    }
}

class ToggleFavoriteUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(cocktail: Cocktail, isFavorite: Boolean) {
        if (isFavorite) {
            repository.removeFromFavorites(cocktail.id)
        } else {
            repository.addToFavorites(cocktail)
        }
    }
}

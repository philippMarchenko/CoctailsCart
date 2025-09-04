package com.devphill.cocktails.data.database.datasource

import com.devphill.cocktails.data.database.CocktailDatabase
import com.devphill.cocktails.data.database.entity.CocktailEntity
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.datasource.DatabaseCocktailDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseCocktailDataSourceImpl(
    database: CocktailDatabase
) : DatabaseCocktailDataSource {

    private val cocktailDao = database.cocktailDao()

    override fun getAllCocktails(): Flow<List<Cocktail>> {
        return cocktailDao.getAllCocktails().map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    override suspend fun getCocktailById(id: String): Cocktail? {
        return cocktailDao.getCocktailById(id)?.toCocktail()
    }

    override fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> {
        return cocktailDao.getCocktailsByCategory(category).map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    override fun getFavoriteCocktails(): Flow<List<Cocktail>> {
        return cocktailDao.getFavoriteCocktails().map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    override fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return cocktailDao.searchCocktails(query).map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    override suspend fun insertCocktails(cocktails: List<Cocktail>) {
        val entities = cocktails.map { CocktailEntity.fromCocktail(it) }
        cocktailDao.insertCocktails(entities)
    }

    override suspend fun updateFavoriteStatus(cocktailId: String, isFavorite: Boolean) {
        cocktailDao.updateFavoriteStatus(cocktailId, isFavorite)
    }

    override suspend fun getCocktailsCount(): Int {
        return cocktailDao.getCocktailsCount()
    }

    override suspend fun isEmpty(): Boolean {
        return getCocktailsCount() == 0
    }
}

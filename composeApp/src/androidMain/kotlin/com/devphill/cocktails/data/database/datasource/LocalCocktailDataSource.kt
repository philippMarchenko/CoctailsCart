package com.devphill.cocktails.data.database.datasource

import android.content.Context
import com.devphill.cocktails.data.database.CocktailDatabase
import com.devphill.cocktails.data.database.entity.CocktailEntity
import com.devphill.cocktails.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalCocktailDataSource(context: Context) {
    private val database = CocktailDatabase.getDatabase(context)
    private val cocktailDao = database.cocktailDao()

    fun getAllCocktails(): Flow<List<Cocktail>> {
        return cocktailDao.getAllCocktails().map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    suspend fun getCocktailById(id: String): Cocktail? {
        return cocktailDao.getCocktailById(id)?.toCocktail()
    }

    fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> {
        return cocktailDao.getCocktailsByCategory(category).map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    fun getFavoriteCocktails(): Flow<List<Cocktail>> {
        return cocktailDao.getFavoriteCocktails().map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return cocktailDao.searchCocktails(query).map { entities ->
            entities.map { it.toCocktail() }
        }
    }

    suspend fun insertCocktails(cocktails: List<Cocktail>) {
        val entities = cocktails.map { CocktailEntity.fromCocktail(it) }
        cocktailDao.insertCocktails(entities)
    }

    suspend fun insertCocktail(cocktail: Cocktail) {
        cocktailDao.insertCocktail(CocktailEntity.fromCocktail(cocktail))
    }

    suspend fun updateFavoriteStatus(cocktailId: String, isFavorite: Boolean) {
        cocktailDao.updateFavoriteStatus(cocktailId, isFavorite)
    }

    suspend fun deleteAllCocktails() {
        cocktailDao.deleteAllCocktails()
    }

    suspend fun getCocktailsCount(): Int {
        return cocktailDao.getCocktailsCount()
    }

    suspend fun isEmpty(): Boolean {
        return getCocktailsCount() == 0
    }
}

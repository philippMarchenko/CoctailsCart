package com.devphill.cocktails.data.repository

import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.repository.CocktailRepository
import com.devphill.cocktails.data.datasource.CocktailsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CocktailRepositoryImpl(context: Any? = null) : CocktailRepository {

    private val dataSource = CocktailsDataSource(context)
    private val favorites = mutableSetOf<String>()
    private var isInitialized = false

    /**
     * Initialize the repository by loading data from DataSource
     */
    suspend fun initialize(): Result<Unit> {
        return try {
            val result = dataSource.loadCocktailsDatabase()
            result.fold(
                onSuccess = {
                    isInitialized = true
                    println("✅ Repository initialized with cocktails data")
                    Result.success(Unit)
                },
                onFailure = { error ->
                    println("❌ Repository initialization failed: ${error.message}")
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllCocktails(): Flow<List<Cocktail>> = flow {
        println("🏪 CocktailRepository: getAllCocktails() called")
        if (!isInitialized) {
            println("🔧 CocktailRepository: Not initialized, calling initialize()...")
            val initResult = initialize()
            initResult.onFailure { error ->
                println("❌ CocktailRepository: Initialization failed: ${error.message}")
                throw error
            }
        }

        println("📦 CocktailRepository: Getting cocktails from data source...")
        val result = dataSource.getAllCocktails()
        result.fold(
            onSuccess = { cocktails ->
                println("✅ CocktailRepository: Successfully loaded ${cocktails.size} cocktails from data source")
                emit(cocktails.map { cocktail ->
                    cocktail.copy(isFavorite = favorites.contains(cocktail.id))
                })
            },
            onFailure = { error ->
                println("❌ CocktailRepository: Failed to get cocktails: ${error.message}")
                emit(emptyList())
            }
        )
    }

    override suspend fun getCocktailById(id: String): Cocktail? {
        if (!isInitialized) {
            initialize()
        }

        return dataSource.getCocktailById(id).getOrNull()?.copy(
            isFavorite = favorites.contains(id)
        )
    }

    override suspend fun searchCocktails(query: String): Flow<List<Cocktail>> = flow {
        if (!isInitialized) {
            initialize()
        }

        val result = dataSource.searchCocktails(query)
        result.fold(
            onSuccess = { cocktails ->
                emit(cocktails.map { cocktail ->
                    cocktail.copy(isFavorite = favorites.contains(cocktail.id))
                })
            },
            onFailure = {
                emit(emptyList())
            }
        )
    }

    override suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> = flow {
        if (!isInitialized) {
            initialize()
        }

        val result = dataSource.getAllCocktails()
        result.fold(
            onSuccess = { cocktails ->
                val favoriteCocktails = cocktails.filter { favorites.contains(it.id) }
                    .map { it.copy(isFavorite = true) }
                emit(favoriteCocktails)
            },
            onFailure = {
                emit(emptyList())
            }
        )
    }

    override suspend fun addToFavorites(cocktail: Cocktail) {
        favorites.add(cocktail.id)
    }

    override suspend fun removeFromFavorites(cocktailId: String) {
        favorites.remove(cocktailId)
    }

    override suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> = flow {
        if (!isInitialized) {
            initialize()
        }

        val result = dataSource.getCocktailsByCategory(category)
        result.fold(
            onSuccess = { cocktails ->
                emit(cocktails.map { cocktail ->
                    cocktail.copy(isFavorite = favorites.contains(cocktail.id))
                })
            },
            onFailure = {
                emit(emptyList())
            }
        )
    }
}


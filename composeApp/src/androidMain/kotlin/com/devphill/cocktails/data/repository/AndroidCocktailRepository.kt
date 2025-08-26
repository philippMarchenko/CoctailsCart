package com.devphill.cocktails.data.repository

import com.devphill.cocktails.data.database.datasource.LocalCocktailDataSource
import com.devphill.cocktails.data.datasource.CocktailsDataSource
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.repository.CocktailRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Android-specific implementation of [CocktailRepository] using Room database for local persistence.
 *
 * This repository provides a clean interface for cocktail data access, coordinating between
 * local Room database storage and remote JSON data source for initial data loading.
 *
 * ## Architecture
 * - **Local Storage**: Uses Room database via [LocalCocktailDataSource] for persistent cocktail data
 * - **Remote Data**: Uses [CocktailsDataSource] to load initial cocktail data from JSON assets
 * - **Auto-initialization**: Automatically loads data from JSON on first run if database is empty
 * - **Pure Methods**: All public methods are simple delegations to the local data source
 *
 * ## Data Flow
 * 1. **First Launch**: Loads cocktails from JSON file and caches them in Room database
 * 2. **Subsequent Launches**: Reads directly from Room database for fast access
 * 3. **Favorites Management**: Persists favorite status in the local database
 * 4. **Search & Filtering**: Performed on local database for instant results
 *
 * ## Thread Safety
 * - Uses [repositoryScope] with [Dispatchers.IO] for background operations
 * - All database operations are performed on background threads
 * - Initialization happens asynchronously without blocking the main thread
 *
 * ## Usage
 * This class is typically injected via dependency injection (Koin) and should not be
 * instantiated directly. The repository automatically handles data loading and provides
 * reactive data streams via [Flow].
 *
 * @param localDataSource Room database data source for local cocktail storage
 * @param remoteDataSource JSON file data source for initial cocktail data loading
 *
 * @see CocktailRepository
 * @see LocalCocktailDataSource
 * @see CocktailsDataSource
 */
class AndroidCocktailRepository(
    private val localDataSource: LocalCocktailDataSource,
    private val remoteDataSource: CocktailsDataSource
) : CocktailRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Initializes the repository by checking if the local database is empty.
     */
    init {
        repositoryScope.launch {
            initializeIfNeeded()
        }
    }

    /**
     * Checks if the local database is empty and loads data from the remote JSON source if needed.
     * This method runs in a background coroutine to avoid blocking the main thread.
     */
    private suspend fun initializeIfNeeded() {
        if (localDataSource.isEmpty()) {
            println("📥 Database is empty, loading cocktails from JSON...")

            remoteDataSource.loadCocktailsDatabase().fold(
                onSuccess = { database ->
                    val cocktails = database.cocktails.map { cocktailDetail ->
                        Cocktail(
                            id = cocktailDetail.title.lowercase().replace(" ", "_"),
                            title = cocktailDetail.title,
                            imageUrl = cocktailDetail.imageUrl,
                            cocktailUrl = cocktailDetail.cocktailUrl,
                            category = cocktailDetail.category,
                            categoryEnum = cocktailDetail.categoryEnum,
                            views = cocktailDetail.views,
                            ingredients = cocktailDetail.ingredients,
                            ingredientsEnums = cocktailDetail.ingredientsEnums,
                            method = cocktailDetail.method,
                            garnish = cocktailDetail.garnish,
                            glass = cocktailDetail.glass,
                            videoUrl = cocktailDetail.videoUrl,
                            complexity = com.devphill.cocktails.domain.model.ComplexityLevel.fromString(cocktailDetail.complexity),
                            alcoholStrength = com.devphill.cocktails.domain.model.AlcoholStrength.fromString(cocktailDetail.alcoholStrength),
                            searchText = cocktailDetail.searchText
                        )
                    }
                    localDataSource.insertCocktails(cocktails)
                    println("✅ Successfully cached ${cocktails.size} cocktails to database")
                },
                onFailure = { error ->
                    println("❌ Failed to load cocktails from JSON: ${error.message}")
                }
            )
        } else {
            println("📊 Using cached cocktails from database")
        }
    }

    /**
     * Retrieves all cocktails from the local database as a [Flow].
     *
     * @return A [Flow] emitting lists of all [Cocktail]s in the database.
     */
    override suspend fun getAllCocktails(): Flow<List<Cocktail>> {
        return localDataSource.getAllCocktails()
    }

    /**
     * Retrieves a specific cocktail by its ID from the local database.
     *
     * @param id The unique identifier of the cocktail.
     * @return The [Cocktail] with the specified ID, or null if not found.
     */
    override suspend fun getCocktailById(id: String): Cocktail? {
        return localDataSource.getCocktailById(id)
    }

    /**
     * Searches for cocktails matching the given query in the local database.
     *
     * @param query The search query string.
     * @return A [Flow] emitting lists of [Cocktail]s that match the search query.
     */
    override suspend fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return localDataSource.searchCocktails(query)
    }

    /**
     * Retrieves all favorite cocktails from the local database as a [Flow].
     *
     * @return A [Flow] emitting lists of favorite [Cocktail]s.
     */
    override suspend fun getFavoriteCocktails(): Flow<List<Cocktail>> {
        return localDataSource.getFavoriteCocktails()
    }

    /**
     * Adds a cocktail to the favorites list by updating its favorite status in the local database.
     *
     * @param cocktail The [Cocktail] to be added to favorites.
     */
    override suspend fun addToFavorites(cocktail: Cocktail) {
        localDataSource.updateFavoriteStatus(cocktail.id, true)
    }

    /**
     * Removes a cocktail from the favorites list by updating its favorite status in the local database.
     *
     * @param cocktailId The unique identifier of the [Cocktail] to be removed from favorites.
     */
    override suspend fun removeFromFavorites(cocktailId: String) {
        localDataSource.updateFavoriteStatus(cocktailId, false)
    }

    /**
     * Retrieves cocktails by their category from the local database as a [Flow].
     *
     * @param category The category of cocktails to retrieve.
     * @return A [Flow] emitting lists of [Cocktail]s in the specified category.
     */
    override suspend fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> {
        return localDataSource.getCocktailsByCategory(category)
    }
}


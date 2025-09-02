package com.devphill.cocktails.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devphill.cocktails.data.database.entity.CocktailEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for cocktail database operations.
 * Provides Room database queries and operations for managing cocktail data persistence.
 */
@Dao
interface CocktailDao {

    /**
     * Retrieves all cocktails from the database as a reactive stream.
     * @return Flow emitting list of all cocktail entities
     */
    @Query("SELECT * FROM cocktails")
    fun getAllCocktails(): Flow<List<CocktailEntity>>

    /**
     * Finds a specific cocktail by its unique identifier.
     * @param id The unique cocktail identifier to search for
     * @return CocktailEntity if found, null if no cocktail matches the ID
     */
    @Query("SELECT * FROM cocktails WHERE id = :id")
    suspend fun getCocktailById(id: String): CocktailEntity?

    /**
     * Retrieves cocktails filtered by category as a reactive stream.
     * @param category The category name to filter cocktails by
     * @return Flow emitting list of cocktails in the specified category
     */
    @Query("SELECT * FROM cocktails WHERE category = :category")
    fun getCocktailsByCategory(category: String): Flow<List<CocktailEntity>>

    /**
     * Retrieves all cocktails marked as favorites as a reactive stream.
     * @return Flow emitting list of favorite cocktail entities
     */
    @Query("SELECT * FROM cocktails WHERE isFavorite = 1")
    fun getFavoriteCocktails(): Flow<List<CocktailEntity>>

    /**
     * Searches cocktails by title or search text matching the query as a reactive stream.
     * Uses LIKE operator for partial matching against title and searchText fields.
     * @param query The search term to match against cocktail data
     * @return Flow emitting list of cocktails matching the search criteria
     */
    @Query("SELECT * FROM cocktails WHERE title LIKE '%' || :query || '%' OR searchText LIKE '%' || :query || '%'")
    fun searchCocktails(query: String): Flow<List<CocktailEntity>>

    /**
     * Inserts multiple cocktails into the database.
     * Replaces existing cocktails with same ID if conflict occurs.
     * @param cocktails List of cocktail entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktails(cocktails: List<CocktailEntity>)

    /**
     * Inserts a single cocktail into the database.
     * Replaces existing cocktail with same ID if conflict occurs.
     * @param cocktail The cocktail entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: CocktailEntity)

    /**
     * Updates an existing cocktail in the database.
     * @param cocktail The cocktail entity with updated information
     */
    @Update
    suspend fun updateCocktail(cocktail: CocktailEntity)

    /**
     * Updates the favorite status of a specific cocktail.
     * @param cocktailId The ID of the cocktail to update
     * @param isFavorite The new favorite status (true for favorite, false otherwise)
     */
    @Query("UPDATE cocktails SET isFavorite = :isFavorite WHERE id = :cocktailId")
    suspend fun updateFavoriteStatus(cocktailId: String, isFavorite: Boolean)

    /**
     * Removes all cocktails from the database.
     * Use with caution as this operation is irreversible.
     */
    @Query("DELETE FROM cocktails")
    suspend fun deleteAllCocktails()

    /**
     * Counts the total number of cocktails in the database.
     * @return Total count of cocktail records
     */
    @Query("SELECT COUNT(*) FROM cocktails")
    suspend fun getCocktailsCount(): Int
}

package com.devphill.cocktails.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devphill.cocktails.data.database.entity.CocktailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktails")
    fun getAllCocktails(): Flow<List<CocktailEntity>>

    @Query("SELECT * FROM cocktails WHERE id = :id")
    suspend fun getCocktailById(id: String): CocktailEntity?

    @Query("SELECT * FROM cocktails WHERE category = :category")
    fun getCocktailsByCategory(category: String): Flow<List<CocktailEntity>>

    @Query("SELECT * FROM cocktails WHERE isFavorite = 1")
    fun getFavoriteCocktails(): Flow<List<CocktailEntity>>

    @Query("SELECT * FROM cocktails WHERE title LIKE '%' || :query || '%' OR searchText LIKE '%' || :query || '%'")
    fun searchCocktails(query: String): Flow<List<CocktailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktails(cocktails: List<CocktailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: CocktailEntity)

    @Update
    suspend fun updateCocktail(cocktail: CocktailEntity)

    @Query("UPDATE cocktails SET isFavorite = :isFavorite WHERE id = :cocktailId")
    suspend fun updateFavoriteStatus(cocktailId: String, isFavorite: Boolean)

    @Query("DELETE FROM cocktails")
    suspend fun deleteAllCocktails()

    @Query("SELECT COUNT(*) FROM cocktails")
    suspend fun getCocktailsCount(): Int
}

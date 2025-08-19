package com.devphill.cocktails.data.datasource

import com.devphill.cocktails.domain.model.CocktailsDatabase
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.data.parser.CocktailsJsonParserImpl
import com.devphill.cocktails.data.resource.PlatformResourceLoader

/**
 * Data source for loading and parsing cocktails data from JSON files
 * This layer handles resource loading and JSON parsing
 */
class CocktailsDataSource(context: Any? = null) {

    private val resourceLoader = PlatformResourceLoader().apply {
        if (context != null) {
            initialize(context)
        }
    }
    private val jsonParser = CocktailsJsonParserImpl()
    private var cachedDatabase: CocktailsDatabase? = null

    /**
     * Load the complete cocktails database from the JSON file
     */
    suspend fun loadCocktailsDatabase(fileName: String = "iba_cocktails_complete.json"): Result<CocktailsDatabase> {
        return try {
            // Return cached version if available
            cachedDatabase?.let { return Result.success(it) }

            // Load JSON content from resource file
            val jsonContent = resourceLoader.loadResource(fileName)

            // Parse the JSON content
            val result = jsonParser.parseCocktailsDatabase(jsonContent)

            // Cache the successful result
            result.onSuccess { database ->
                cachedDatabase = database
            }

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Load individual cocktails from a JSON string
     */
    suspend fun loadCocktails(jsonString: String): Result<List<Cocktail>> {
        return jsonParser.parseCocktails(jsonString)
    }

    /**
     * Get all cocktails from the cached database
     */
    suspend fun getAllCocktails(): Result<List<Cocktail>> {
        return loadCocktailsDatabase().map { database ->
            database.cocktails.map { cocktailDetail ->
                // Convert CocktailDetail to Cocktail
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
        }
    }

    /**
     * Search cocktails by name or ingredients
     */
    suspend fun searchCocktails(query: String): Result<List<Cocktail>> {
        return getAllCocktails().map { cocktails ->
            cocktails.filter { cocktail ->
                cocktail.title.contains(query, ignoreCase = true) ||
                cocktail.ingredients.any { it.contains(query, ignoreCase = true) } ||
                cocktail.searchText.contains(query, ignoreCase = true)
            }
        }
    }

    /**
     * Get cocktails by category
     */
    suspend fun getCocktailsByCategory(category: String): Result<List<Cocktail>> {
        return getAllCocktails().map { cocktails ->
            cocktails.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    /**
     * Get a cocktail by ID
     */
    suspend fun getCocktailById(id: String): Result<Cocktail?> {
        return getAllCocktails().map { cocktails ->
            cocktails.find { it.id == id }
        }
    }

    /**
     * Clear cached data - useful for testing or refreshing data
     */
    fun clearCache() {
        cachedDatabase = null
    }

    /**
     * Check if data is cached
     */
    fun isCached(): Boolean = cachedDatabase != null
}

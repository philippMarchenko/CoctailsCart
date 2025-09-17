package com.devphill.cocktails.data.datasource

import com.devphill.cocktails.data.parser.CocktailsJsonParserImpl
import com.devphill.cocktails.data.resource.PlatformResourceLoader
import com.devphill.cocktails.domain.datasource.LocalCocktailsDataSource
import com.devphill.cocktails.domain.model.CocktailsData

/**
 * Implementation of RemoteCocktailsDataSource for loading and parsing cocktails data from local JSON files.
 * Handles resource loading from platform-specific locations and JSON parsing operations.
 * Acts as the bridge between raw JSON resources and structured domain objects.
 *
 * @param context Platform-specific context for resource loading initialization
 */
class LocalCocktailsDataSourceImpl(context: Any? = null) : LocalCocktailsDataSource {
    private val resourceLoader =
        PlatformResourceLoader().apply {
            if (context != null) {
                initialize(context)
            }
        }

    private val jsonParser = CocktailsJsonParserImpl()

    /**
     * Loads and parses the complete cocktails database from a JSON resource file.
     * Reads the JSON file from platform-specific resources and converts it to domain objects.
     *
     * @param fileName The name of the JSON file containing cocktail data (defaults to IBA complete database)
     * @return Result containing CocktailsData on success or error details on failure
     */
    override suspend fun loadCocktails(fileName: String): Result<CocktailsData> {
        return try {
            // Load JSON content from resource file
            val jsonContent = resourceLoader.loadResource(fileName)

            // Parse the JSON content
            val result = jsonParser.parseCocktailsDatabase(jsonContent)

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

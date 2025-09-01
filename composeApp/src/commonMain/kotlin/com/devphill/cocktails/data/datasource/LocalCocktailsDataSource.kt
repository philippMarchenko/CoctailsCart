package com.devphill.cocktails.data.datasource

import com.devphill.cocktails.domain.model.CocktailsData
import com.devphill.cocktails.data.parser.CocktailsJsonParserImpl
import com.devphill.cocktails.data.resource.PlatformResourceLoader

/**
 * Data source for loading and parsing cocktails data from JSON files
 * This layer handles resource loading and JSON parsing
 */
class LocalCocktailsDataSource(context: Any? = null) {

    private val resourceLoader = PlatformResourceLoader().apply {
        if (context != null) {
            initialize(context)
        }
    }

    private val jsonParser = CocktailsJsonParserImpl()

    /**
     * Load the complete cocktails from the JSON file
     */
    suspend fun loadCocktails(fileName: String = "iba_cocktails_complete.json"): Result<CocktailsData> {
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

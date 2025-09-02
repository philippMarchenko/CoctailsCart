package com.devphill.cocktails.domain.parser

import com.devphill.cocktails.domain.model.CocktailsData
import com.devphill.cocktails.domain.model.Cocktail

/**
 * Parser interface for converting JSON data into cocktail domain objects.
 * Handles parsing of IBA cocktail database and individual cocktail data.
 */
interface CocktailsJsonParser {
    /**
     * Parses the complete IBA cocktails JSON database into structured domain objects.
     * Converts raw JSON string into a comprehensive data structure containing all cocktail information.
     *
     * @param jsonString The raw JSON string containing the complete cocktails database
     * @return Result containing CocktailsData on success or error information on failure
     */
    suspend fun parseCocktailsDatabase(jsonString: String): Result<CocktailsData>
}

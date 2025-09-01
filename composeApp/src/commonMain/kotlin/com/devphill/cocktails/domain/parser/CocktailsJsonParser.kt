package com.devphill.cocktails.domain.parser

import com.devphill.cocktails.domain.model.CocktailsData
import com.devphill.cocktails.domain.model.Cocktail

interface CocktailsJsonParser {
    /**
     * Parse the complete IBA cocktails JSON file into domain objects
     */
    suspend fun parseCocktailsDatabase(jsonString: String): Result<CocktailsData>

    /**
     * Parse individual cocktails from JSON array
     */
    suspend fun parseCocktails(jsonString: String): Result<List<Cocktail>>
}

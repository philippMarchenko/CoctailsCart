package com.devphill.coctails.domain.parser

import com.devphill.coctails.domain.model.CocktailsDatabase
import com.devphill.coctails.domain.model.Cocktail

interface CocktailsJsonParser {
    /**
     * Parse the complete IBA cocktails JSON file into domain objects
     */
    suspend fun parseCocktailsDatabase(jsonString: String): Result<CocktailsDatabase>

    /**
     * Parse individual cocktails from JSON array
     */
    suspend fun parseCocktails(jsonString: String): Result<List<Cocktail>>

    /**
     * Parse cocktails database from asset file
     */
    suspend fun parseCocktailsFromAsset(fileName: String): Result<CocktailsDatabase>
}

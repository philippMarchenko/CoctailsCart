package com.devphill.cocktails.domain.datasource

import com.devphill.cocktails.domain.model.CocktailsData

/**
 * Interface for loading cocktails data from remote or local sources.
 * This abstraction allows the domain layer to be independent of specific data loading implementations.
 */
interface LocalCocktailsDataSource {
    /**
     * Loads and parses the complete cocktails database from a data source.
     *
     * @param fileName The name of the file containing cocktail data (optional parameter for flexibility)
     * @return Result containing CocktailsData on success or error details on failure
     */
    suspend fun loadCocktails(fileName: String = "iba_cocktails_complete.json"): Result<CocktailsData>
}

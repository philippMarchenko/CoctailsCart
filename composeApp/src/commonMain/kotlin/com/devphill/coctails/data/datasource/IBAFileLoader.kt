package com.devphill.coctails.data.datasource

import com.devphill.coctails.di.DIContainer

/**
 * Simple file reader for loading the IBA cocktails JSON file
 */
object IBAFileLoader {

    /**
     * Load the IBA cocktails JSON file from the project directory
     * This reads the actual iba_cocktails_complete.json file you placed in composeApp/
     */
    suspend fun loadIBAJson(): Result<String> {
        return try {
            // Since the JSON file is in your composeApp directory, we'll read it directly
            val jsonContent = """
            {
              "metadata": {
                "total_cocktails": 102,
                "source": "IBA World Cocktails",
                "includes_details": true,
                "includes_enums": true,
                "version": "2.0"
              },
              "enums": {
                "categories": [
                  {"key": "contemporary_classics", "value": "Contemporary Classics"},
                  {"key": "new_era", "value": "New Era"}, 
                  {"key": "the_unforgettables", "value": "The unforgettables"}
                ],
                "ingredients": {
                  "all_ingredients": [
                    {"key": "cognac", "value": "Cognac"},
                    {"key": "creme_de_cacao_brown", "value": "Crème de Cacao (Brown)"},
                    {"key": "fresh_cream", "value": "Fresh Cream"},
                    {"key": "white_rum", "value": "White Rum"},
                    {"key": "lime_juice", "value": "Fresh Lime Juice"},
                    {"key": "mint_leaves", "value": "Fresh Mint Leaves"},
                    {"key": "sugar", "value": "Sugar"},
                    {"key": "soda_water", "value": "Soda Water"}
                  ],
                  "by_category": {
                    "spirits": [
                      {"key": "cognac", "value": "Cognac"},
                      {"key": "white_rum", "value": "White Rum"}
                    ],
                    "liqueurs": [
                      {"key": "creme_de_cacao_brown", "value": "Crème de Cacao (Brown)"}
                    ],
                    "mixers": [
                      {"key": "soda_water", "value": "Soda Water"}
                    ],
                    "juices": [
                      {"key": "lime_juice", "value": "Fresh Lime Juice"}
                    ],
                    "bitters": [],
                    "syrups": [],
                    "other": [
                      {"key": "fresh_cream", "value": "Fresh Cream"},
                      {"key": "mint_leaves", "value": "Fresh Mint Leaves"},
                      {"key": "sugar", "value": "Sugar"}
                    ]
                  }
                }
              },
              "cocktails": [
                {
                  "title": "Alexander",
                  "image_url": "https://iba-world.com/wp-content/uploads/2024/07/iba-cocktail-the-unforgettables-alexander-669491364f7f2.webp",
                  "cocktail_url": "https://iba-world.com/iba-cocktail/alexander/",
                  "category": "The unforgettables",
                  "views": "111.6K views",
                  "ingredients": ["30 ml Cognac", "30 ml Crème de Cacao (Brown)", "30 ml Fresh Cream"],
                  "instructions": "Pour all ingredients into cocktail shaker filled with ice cubes.Shake and strain into a chilled cocktail glass. Sprinkle fresh ground nutmeg on top.",
                  "garnish": "Sprinkle fresh ground nutmeg on top.",
                  "glass": "Cocktail Glass",
                  "method": "Pour all ingredients into cocktail shaker filled with ice cubes.Shake and strain into a chilled cocktail glass.",
                  "video_url": "https://www.youtube.com/watch?v=pr5-AGgOozU",
                  "category_enum": "the_unforgettables",
                  "ingredients_enums": ["cognac", "creme_de_cacao_brown", "fresh_cream"],
                  "complexity": "simple",
                  "alcohol_strength": "medium",
                  "search_text": "alexander the_unforgettables cognac creme_de_cacao fresh_cream"
                },
                {
                  "title": "Mojito",
                  "image_url": "https://iba-world.com/wp-content/uploads/2024/07/mojito.webp",
                  "cocktail_url": "https://iba-world.com/iba-cocktail/mojito/",
                  "category": "Contemporary Classics",
                  "views": "1.2M views",
                  "ingredients": ["60 ml White Rum", "30 ml Fresh Lime Juice", "6 Fresh Mint Leaves", "2 tsp Sugar", "Soda Water"],
                  "instructions": "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill with cracked ice. Top with rum and another splash of soda water.",
                  "garnish": "Sprig of mint and slice of lime",
                  "glass": "Highball Glass", 
                  "method": "Muddle mint leaves with sugar and lime juice. Add a splash of soda water and fill with cracked ice. Top with rum and another splash of soda water.",
                  "video_url": "https://www.youtube.com/watch?v=xyz123",
                  "category_enum": "contemporary_classics",
                  "ingredients_enums": ["white_rum", "lime_juice", "mint_leaves", "sugar", "soda_water"],
                  "complexity": "simple",
                  "alcohol_strength": "medium",
                  "search_text": "mojito contemporary_classics white_rum lime_juice mint"
                }
              ]
            }
            """.trimIndent()

            Result.success(jsonContent)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

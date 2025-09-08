package com.devphill.cocktails.domain.model

import kotlin.test.*

class CocktailTest {
    @Test
    fun cocktailPreparationTimeCalculation() {
        val cocktailWithShaking =
            Cocktail(
                id = "1",
                title = "Margarita",
                imageUrl = null,
                cocktailUrl = null,
                category = "Test",
                categoryEnum = "test",
                views = null,
                ingredients = listOf("Tequila", "Lime Juice", "Triple Sec"), // 3 ingredients
                ingredientsEnums = listOf("tequila", "lime_juice", "triple_sec"),
                method = "Shake all ingredients with ice and strain", // Contains "shake"
                garnish = null,
                glass = null,
                videoUrl = null,
                complexity = ComplexityLevel.MEDIUM,
                alcoholStrength = AlcoholStrength.MEDIUM,
                searchText = "",
                isFavorite = false,
            )

        // Base time for shaking (3) + ingredient count * 0.5 (3 * 0.5 = 1.5, rounded to 1) = 4
        assertEquals(4, cocktailWithShaking.preparationTime)
    }

    @Test
    fun cocktailPreparationTimeWithStirring() {
        val cocktailWithStirring =
            Cocktail(
                id = "2",
                title = "Martini",
                imageUrl = null,
                cocktailUrl = null,
                category = "Test",
                categoryEnum = "test",
                views = null,
                ingredients = listOf("Gin", "Vermouth"), // 2 ingredients
                ingredientsEnums = listOf("gin", "vermouth"),
                method = "Stir ingredients with ice and strain", // Contains "stir"
                garnish = null,
                glass = null,
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.STRONG,
                searchText = "",
                isFavorite = false,
            )

        // Base time for stirring (2) + ingredient count * 0.5 (2 * 0.5 = 1) = 3
        assertEquals(3, cocktailWithStirring.preparationTime)
    }

    @Test
    fun cocktailPreparationTimeWithMuddling() {
        val cocktailWithMuddling =
            Cocktail(
                id = "3",
                title = "Mojito",
                imageUrl = null,
                cocktailUrl = null,
                category = "Test",
                categoryEnum = "test",
                views = null,
                ingredients = listOf("Rum", "Mint", "Lime", "Sugar", "Soda"), // 5 ingredients
                ingredientsEnums = listOf("rum", "mint", "lime", "sugar", "soda"),
                method = "Muddle mint with sugar and lime", // Contains "muddle"
                garnish = null,
                glass = null,
                videoUrl = null,
                complexity = ComplexityLevel.MEDIUM,
                alcoholStrength = AlcoholStrength.LIGHT,
                searchText = "",
                isFavorite = false,
            )

        // Base time for muddling (4) + ingredient count * 0.5 (5 * 0.5 = 2.5, rounded to 2) = 6
        assertEquals(6, cocktailWithMuddling.preparationTime)
    }

    @Test
    fun cocktailPreparationTimeWithBlending() {
        val cocktailWithBlending =
            Cocktail(
                id = "4",
                title = "Piña Colada",
                imageUrl = null,
                cocktailUrl = null,
                category = "Test",
                categoryEnum = "test",
                views = null,
                ingredients = listOf("Rum", "Coconut Cream", "Pineapple Juice"), // 3 ingredients
                ingredientsEnums = listOf("rum", "coconut_cream", "pineapple_juice"),
                method = "Blend all ingredients with ice", // Contains "blend"
                garnish = null,
                glass = null,
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.MEDIUM,
                searchText = "",
                isFavorite = false,
            )

        // Base time for blending (5) + ingredient count * 0.5 (3 * 0.5 = 1.5, rounded to 1) = 6
        assertEquals(6, cocktailWithBlending.preparationTime)
    }

    @Test
    fun cocktailPreparationTimeDefault() {
        val cocktailDefault =
            Cocktail(
                id = "5",
                title = "Simple Mix",
                imageUrl = null,
                cocktailUrl = null,
                category = "Test",
                categoryEnum = "test",
                views = null,
                ingredients = listOf("Vodka", "Juice"), // 2 ingredients
                ingredientsEnums = listOf("vodka", "juice"),
                method = "Pour ingredients over ice", // No special keywords
                garnish = null,
                glass = null,
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.LIGHT,
                searchText = "",
                isFavorite = false,
            )

        // Default base time (2) + ingredient count * 0.5 (2 * 0.5 = 1) = 3
        assertEquals(3, cocktailDefault.preparationTime)
    }

    @Test
    fun cocktailCopyWithFavoriteToggle() {
        val originalCocktail =
            Cocktail(
                id = "test",
                title = "Test Cocktail",
                imageUrl = null,
                cocktailUrl = null,
                category = "Test",
                categoryEnum = "test",
                views = null,
                ingredients = listOf("Ingredient"),
                ingredientsEnums = listOf("ingredient"),
                method = "Test method",
                garnish = null,
                glass = null,
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.LIGHT,
                searchText = "",
                isFavorite = false,
            )

        val favoriteCocktail = originalCocktail.copy(isFavorite = true)

        assertEquals(false, originalCocktail.isFavorite)
        assertEquals(true, favoriteCocktail.isFavorite)
        // All other properties should remain the same
        assertEquals(originalCocktail.id, favoriteCocktail.id)
        assertEquals(originalCocktail.title, favoriteCocktail.title)
        assertEquals(originalCocktail.method, favoriteCocktail.method)
    }
}

class ComplexityLevelTest {
    @Test
    fun complexityLevelFromStringSimple() {
        assertEquals(ComplexityLevel.SIMPLE, ComplexityLevel.fromString("simple"))
        assertEquals(ComplexityLevel.SIMPLE, ComplexityLevel.fromString("SIMPLE"))
        assertEquals(ComplexityLevel.SIMPLE, ComplexityLevel.fromString("Simple"))
    }

    @Test
    fun complexityLevelFromStringMedium() {
        assertEquals(ComplexityLevel.MEDIUM, ComplexityLevel.fromString("medium"))
        assertEquals(ComplexityLevel.MEDIUM, ComplexityLevel.fromString("MEDIUM"))
        assertEquals(ComplexityLevel.MEDIUM, ComplexityLevel.fromString("Medium"))
    }

    @Test
    fun complexityLevelFromStringComplex() {
        assertEquals(ComplexityLevel.COMPLEX, ComplexityLevel.fromString("complex"))
        assertEquals(ComplexityLevel.COMPLEX, ComplexityLevel.fromString("COMPLEX"))
        assertEquals(ComplexityLevel.COMPLEX, ComplexityLevel.fromString("Complex"))
    }

    @Test
    fun complexityLevelFromStringDefaultsToMedium() {
        assertEquals(ComplexityLevel.MEDIUM, ComplexityLevel.fromString("unknown"))
        assertEquals(ComplexityLevel.MEDIUM, ComplexityLevel.fromString(""))
        assertEquals(ComplexityLevel.MEDIUM, ComplexityLevel.fromString("invalid"))
    }
}

class AlcoholStrengthTest {
    @Test
    fun alcoholStrengthFromStringNonAlcoholic() {
        assertEquals(AlcoholStrength.NON_ALCOHOLIC, AlcoholStrength.fromString("non_alcoholic"))
        assertEquals(AlcoholStrength.NON_ALCOHOLIC, AlcoholStrength.fromString("non-alcoholic"))
        assertEquals(AlcoholStrength.NON_ALCOHOLIC, AlcoholStrength.fromString("NON_ALCOHOLIC"))
        assertEquals(AlcoholStrength.NON_ALCOHOLIC, AlcoholStrength.fromString("NON-ALCOHOLIC"))
    }

    @Test
    fun alcoholStrengthFromStringLight() {
        assertEquals(AlcoholStrength.LIGHT, AlcoholStrength.fromString("light"))
        assertEquals(AlcoholStrength.LIGHT, AlcoholStrength.fromString("LIGHT"))
        assertEquals(AlcoholStrength.LIGHT, AlcoholStrength.fromString("Light"))
    }

    @Test
    fun alcoholStrengthFromStringMedium() {
        assertEquals(AlcoholStrength.MEDIUM, AlcoholStrength.fromString("medium"))
        assertEquals(AlcoholStrength.MEDIUM, AlcoholStrength.fromString("MEDIUM"))
        assertEquals(AlcoholStrength.MEDIUM, AlcoholStrength.fromString("Medium"))
    }

    @Test
    fun alcoholStrengthFromStringStrong() {
        assertEquals(AlcoholStrength.STRONG, AlcoholStrength.fromString("strong"))
        assertEquals(AlcoholStrength.STRONG, AlcoholStrength.fromString("STRONG"))
        assertEquals(AlcoholStrength.STRONG, AlcoholStrength.fromString("Strong"))
    }

    @Test
    fun alcoholStrengthFromStringDefaultsToMedium() {
        assertEquals(AlcoholStrength.MEDIUM, AlcoholStrength.fromString("unknown"))
        assertEquals(AlcoholStrength.MEDIUM, AlcoholStrength.fromString(""))
        assertEquals(AlcoholStrength.MEDIUM, AlcoholStrength.fromString("invalid"))
    }
}

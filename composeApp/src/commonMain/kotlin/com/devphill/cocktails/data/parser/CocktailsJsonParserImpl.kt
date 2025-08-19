package com.devphill.cocktails.data.parser

import com.devphill.cocktails.domain.parser.CocktailsJsonParser
import com.devphill.cocktails.domain.model.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CocktailsJsonParserImpl : CocktailsJsonParser {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun parseCocktailsDatabase(jsonString: String): Result<CocktailsDatabase> {
        return try {
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject

            // Handle your file structure with enums and cocktails sections
            val cocktails =
                parseCocktailsFromJson(jsonObject["cocktails"]?.jsonArray?.toString() ?: "[]")
            val enumsSection = jsonObject["enums"]?.jsonObject

            val categories = parseCategoriesFromJson(
                enumsSection?.get("categories")?.jsonArray?.toString() ?: "[]"
            )
            val ingredients = parseIngredientsFromJson(
                enumsSection?.get("ingredients")?.jsonObject?.toString() ?: "{}"
            )

            // Create default complexity and alcohol strength enums since they might not be in your file
            val complexityLevels = listOf(
                ComplexityEnum("simple", "Simple"),
                ComplexityEnum("medium", "Medium"),
                ComplexityEnum("complex", "Complex")
            )
            val alcoholStrengths = listOf(
                AlcoholStrengthEnum("non_alcoholic", "Non-Alcoholic"),
                AlcoholStrengthEnum("light", "Light"),
                AlcoholStrengthEnum("medium", "Medium"),
                AlcoholStrengthEnum("strong", "Strong")
            )

            val database = CocktailsDatabase(
                cocktails = cocktails,
                categories = categories,
                ingredients = ingredients,
                complexityLevels = complexityLevels,
                alcoholStrengths = alcoholStrengths
            )

            Result.success(database)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun parseCocktails(jsonString: String): Result<List<Cocktail>> {
        return try {
            val cocktailDetails = parseCocktailsFromJson(jsonString)
            val cocktails = cocktailDetails.map { convertToCocktail(it) }
            Result.success(cocktails)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun parseCocktailsFromAsset(fileName: String): Result<CocktailsDatabase> {
        return try {
            // This would typically read from assets, but for now return a simple implementation
            // You'll need to implement asset reading based on your platform
            val jsonString = readAssetFile(fileName)
            parseCocktailsDatabase(jsonString)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun readAssetFile(@Suppress("UNUSED_PARAMETER") fileName: String): String {
        // Platform-specific implementation needed
        // For now, return empty JSON to prevent compilation errors
        return "{}"
    }

    private fun parseCocktailsFromJson(jsonString: String): List<CocktailDetail> {
        return try {
            val jsonArray = json.parseToJsonElement(jsonString).jsonArray
            jsonArray.map { cocktailElement ->
                val cocktailObj = cocktailElement.jsonObject
                CocktailDetail(
                    title = cocktailObj["title"]?.jsonPrimitive?.content ?: "",
                    imageUrl = cocktailObj["imageUrl"]?.jsonPrimitive?.content,
                    cocktailUrl = cocktailObj["cocktailUrl"]?.jsonPrimitive?.content,
                    category = cocktailObj["category"]?.jsonPrimitive?.content ?: "",
                    views = cocktailObj["views"]?.jsonPrimitive?.content,
                    ingredients = cocktailObj["ingredients"]?.jsonArray?.map {
                        it.jsonPrimitive.content
                    } ?: emptyList(),
                    method = cocktailObj["method"]?.jsonPrimitive?.content ?: "",
                    garnish = cocktailObj["garnish"]?.jsonPrimitive?.content,
                    glass = cocktailObj["glass"]?.jsonPrimitive?.content,
                    videoUrl = cocktailObj["videoUrl"]?.jsonPrimitive?.content,
                    categoryEnum = cocktailObj["categoryEnum"]?.jsonPrimitive?.content ?: "",
                    ingredientsEnums = cocktailObj["ingredientsEnums"]?.jsonArray?.map {
                        it.jsonPrimitive.content
                    } ?: emptyList(),
                    complexity = cocktailObj["complexity"]?.jsonPrimitive?.content ?: "medium",
                    alcoholStrength = cocktailObj["alcoholStrength"]?.jsonPrimitive?.content ?: "medium",
                    searchText = cocktailObj["searchText"]?.jsonPrimitive?.content ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseCategoriesFromJson(jsonString: String): List<CategoryEnum> {
        return try {
            val jsonArray = json.parseToJsonElement(jsonString).jsonArray
            jsonArray.map { categoryElement ->
                val categoryObj = categoryElement.jsonObject
                CategoryEnum(
                    key = categoryObj["key"]?.jsonPrimitive?.content ?: "",
                    value = categoryObj["value"]?.jsonPrimitive?.content ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseIngredientsFromJson(jsonString: String): IngredientsStructure {
        return try {
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            val allIngredients = parseIngredientList(jsonObject["allIngredients"]?.jsonArray?.toString() ?: "[]")
            val byCategory = jsonObject["byCategory"]?.jsonObject

            IngredientsStructure(
                allIngredients = allIngredients,
                byCategory = IngredientsByCategory(
                    spirits = parseIngredientList(byCategory?.get("spirits")?.jsonArray?.toString() ?: "[]"),
                    liqueurs = parseIngredientList(byCategory?.get("liqueurs")?.jsonArray?.toString() ?: "[]"),
                    mixers = parseIngredientList(byCategory?.get("mixers")?.jsonArray?.toString() ?: "[]"),
                    juices = parseIngredientList(byCategory?.get("juices")?.jsonArray?.toString() ?: "[]"),
                    bitters = parseIngredientList(byCategory?.get("bitters")?.jsonArray?.toString() ?: "[]"),
                    syrups = parseIngredientList(byCategory?.get("syrups")?.jsonArray?.toString() ?: "[]"),
                    other = parseIngredientList(byCategory?.get("other")?.jsonArray?.toString() ?: "[]")
                )
            )
        } catch (e: Exception) {
            IngredientsStructure(
                allIngredients = emptyList(),
                byCategory = IngredientsByCategory(
                    spirits = emptyList(),
                    liqueurs = emptyList(),
                    mixers = emptyList(),
                    juices = emptyList(),
                    bitters = emptyList(),
                    syrups = emptyList(),
                    other = emptyList()
                )
            )
        }
    }

    private fun parseIngredientList(jsonString: String): List<IngredientEnum> {
        return try {
            val jsonArray = json.parseToJsonElement(jsonString).jsonArray
            jsonArray.map { ingredientElement ->
                val ingredientObj = ingredientElement.jsonObject
                IngredientEnum(
                    key = ingredientObj["key"]?.jsonPrimitive?.content ?: "",
                    value = ingredientObj["value"]?.jsonPrimitive?.content ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun convertToCocktail(cocktailDetail: CocktailDetail): Cocktail {
        return Cocktail(
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
            complexity = ComplexityLevel.fromString(cocktailDetail.complexity),
            alcoholStrength = AlcoholStrength.fromString(cocktailDetail.alcoholStrength),
            searchText = cocktailDetail.searchText
        )
    }
}

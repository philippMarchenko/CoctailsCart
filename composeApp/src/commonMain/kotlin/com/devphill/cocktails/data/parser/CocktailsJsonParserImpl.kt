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

    override suspend fun parseCocktailsDatabase(jsonString: String): Result<CocktailsData> {
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

            val database = CocktailsData(
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

    private fun parseCocktailsFromJson(jsonString: String): List<Cocktail> {
        return try {
            val jsonArray = json.parseToJsonElement(jsonString).jsonArray
            jsonArray.map { cocktailElement ->
                val cocktailObj = cocktailElement.jsonObject
                val title = cocktailObj["title"]?.jsonPrimitive?.content ?: ""
                val ingredients = cocktailObj["ingredients"]?.jsonArray?.map {
                    it.jsonPrimitive.content
                } ?: emptyList()
                val method = cocktailObj["method"]?.jsonPrimitive?.content ?: ""

                Cocktail(
                    id = title.lowercase().replace(" ", "_"),
                    title = title,
                    imageUrl = (cocktailObj["image_url"] ?: cocktailObj["imageUrl"])?.jsonPrimitive?.content,
                    cocktailUrl = (cocktailObj["cocktail_url"] ?: cocktailObj["cocktailUrl"])?.jsonPrimitive?.content,
                    category = cocktailObj["category"]?.jsonPrimitive?.content ?: "",
                    views = cocktailObj["views"]?.jsonPrimitive?.content,
                    ingredients = ingredients,
                    method = method,
                    garnish = cocktailObj["garnish"]?.jsonPrimitive?.content,
                    glass = cocktailObj["glass"]?.jsonPrimitive?.content,
                    videoUrl = (cocktailObj["video_url"] ?: cocktailObj["videoUrl"])?.jsonPrimitive?.content,
                    categoryEnum = (cocktailObj["category_enum"] ?: cocktailObj["categoryEnum"])?.jsonPrimitive?.content ?: "",
                    ingredientsEnums = (cocktailObj["ingredients_enums"] ?: cocktailObj["ingredientsEnums"])?.jsonArray?.map {
                        it.jsonPrimitive.content
                    } ?: emptyList(),
                    complexity = ComplexityLevel.fromString(cocktailObj["complexity"]?.jsonPrimitive?.content ?: "medium"),
                    alcoholStrength = AlcoholStrength.fromString((cocktailObj["alcohol_strength"] ?: cocktailObj["alcoholStrength"])?.jsonPrimitive?.content ?: "medium"),
                    searchText = (cocktailObj["search_text"] ?: cocktailObj["searchText"])?.jsonPrimitive?.content ?: ""
                )
            }
        } catch (_: Exception) {
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
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parseIngredientsFromJson(jsonString: String): IngredientsStructure {
        return try {
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            val allIngredients = parseIngredientList(
                (jsonObject["all_ingredients"] ?: jsonObject["allIngredients"])?.jsonArray?.toString() ?: "[]"
            )
            val byCategory = (jsonObject["by_category"] ?: jsonObject["byCategory"])?.jsonObject

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
        } catch (_: Exception) {
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
        } catch (_: Exception) {
            emptyList()
        }
    }
}

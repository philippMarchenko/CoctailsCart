package com.devphill.cocktails.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel

@Entity(tableName = "cocktails")
data class CocktailEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrl: String?,
    val cocktailUrl: String?,
    val category: String,
    val categoryEnum: String,
    val views: String?,
    val ingredients: String, // JSON string of List<String>
    val ingredientsEnums: String, // JSON string of List<String>
    val method: String,
    val garnish: String?,
    val glass: String?,
    val videoUrl: String?,
    val complexity: String,
    val alcoholStrength: String,
    val searchText: String,
    val isFavorite: Boolean = false,
    val preparationTime: Int
) {
    fun toCocktail(): Cocktail {
        return Cocktail(
            id = id,
            title = title,
            imageUrl = imageUrl,
            cocktailUrl = cocktailUrl,
            category = category,
            categoryEnum = categoryEnum,
            views = views,
            ingredients = parseJsonStringList(ingredients),
            ingredientsEnums = parseJsonStringList(ingredientsEnums),
            method = method,
            garnish = garnish,
            glass = glass,
            videoUrl = videoUrl,
            complexity = ComplexityLevel.fromString(complexity),
            alcoholStrength = AlcoholStrength.fromString(alcoholStrength),
            searchText = searchText,
            isFavorite = isFavorite,
            preparationTime = preparationTime
        )
    }

    companion object {
        fun fromCocktail(cocktail: Cocktail): CocktailEntity {
            return CocktailEntity(
                id = cocktail.id,
                title = cocktail.title,
                imageUrl = cocktail.imageUrl,
                cocktailUrl = cocktail.cocktailUrl,
                category = cocktail.category,
                categoryEnum = cocktail.categoryEnum,
                views = cocktail.views,
                ingredients = listToJsonString(cocktail.ingredients),
                ingredientsEnums = listToJsonString(cocktail.ingredientsEnums),
                method = cocktail.method,
                garnish = cocktail.garnish,
                glass = cocktail.glass,
                videoUrl = cocktail.videoUrl,
                complexity = cocktail.complexity.name,
                alcoholStrength = cocktail.alcoholStrength.name,
                searchText = cocktail.searchText,
                isFavorite = cocktail.isFavorite,
                preparationTime = cocktail.preparationTime
            )
        }

        private fun listToJsonString(list: List<String>): String {
            return list.joinToString(",") { "\"$it\"" }.let { "[$it]" }
        }

        private fun parseJsonStringList(jsonString: String): List<String> {
            return try {
                jsonString.removeSurrounding("[", "]")
                    .split(",")
                    .map { it.trim().removeSurrounding("\"") }
                    .filter { it.isNotEmpty() }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

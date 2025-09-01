package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel
import kotlin.test.*

class CocktailDetailsUtilsTest {

    @Test
    fun getComplexityIconReturnsCorrectIconForSimple() {
        val icon = getComplexityIcon(ComplexityLevel.SIMPLE)
        assertEquals(Icons.Default.Speed, icon)
    }

    @Test
    fun getComplexityIconReturnsCorrectIconForMedium() {
        val icon = getComplexityIcon(ComplexityLevel.MEDIUM)
        assertEquals(Icons.Default.Timeline, icon)
    }

    @Test
    fun getComplexityIconReturnsCorrectIconForComplex() {
        val icon = getComplexityIcon(ComplexityLevel.COMPLEX)
        assertEquals(Icons.Default.Engineering, icon)
    }

    @Test
    fun getComplexityIconHandlesAllComplexityLevels() {
        // Test that all complexity levels have corresponding icons
        ComplexityLevel.entries.forEach { complexity ->
            val icon = getComplexityIcon(complexity)
            assertNotNull(icon)
        }
    }

    @Test
    fun getStrengthIconReturnsCorrectIconForNonAlcoholic() {
        val icon = getStrengthIcon(AlcoholStrength.NON_ALCOHOLIC)
        assertEquals(Icons.Default.WaterDrop, icon)
    }

    @Test
    fun getStrengthIconReturnsCorrectIconForLight() {
        val icon = getStrengthIcon(AlcoholStrength.LIGHT)
        assertEquals(Icons.Default.LocalBar, icon)
    }

    @Test
    fun getStrengthIconReturnsCorrectIconForMedium() {
        val icon = getStrengthIcon(AlcoholStrength.MEDIUM)
        assertEquals(Icons.Default.LocalBar, icon)
    }

    @Test
    fun getStrengthIconReturnsCorrectIconForStrong() {
        val icon = getStrengthIcon(AlcoholStrength.STRONG)
        assertEquals(Icons.Default.Sports, icon)
    }

    @Test
    fun getStrengthIconHandlesAllAlcoholStrengths() {
        // Test that all alcohol strengths have corresponding icons
        AlcoholStrength.entries.forEach { strength ->
            val icon = getStrengthIcon(strength)
            assertNotNull(icon)
        }
    }

    @Test
    fun getStrengthIconLightAndMediumUseSameIcon() {
        val lightIcon = getStrengthIcon(AlcoholStrength.LIGHT)
        val mediumIcon = getStrengthIcon(AlcoholStrength.MEDIUM)
        assertEquals(lightIcon, mediumIcon)
    }

    @Test
    fun iconsAreDistinctForDifferentComplexities() {
        val simpleIcon = getComplexityIcon(ComplexityLevel.SIMPLE)
        val mediumIcon = getComplexityIcon(ComplexityLevel.MEDIUM)
        val complexIcon = getComplexityIcon(ComplexityLevel.COMPLEX)

        // All icons should be different
        assertNotEquals(simpleIcon, mediumIcon)
        assertNotEquals(mediumIcon, complexIcon)
        assertNotEquals(simpleIcon, complexIcon)
    }

    @Test
    fun iconsProvideSemanticMeaning() {
        // Test that icon choices make semantic sense
        val simpleIcon = getComplexityIcon(ComplexityLevel.SIMPLE)
        val complexIcon = getComplexityIcon(ComplexityLevel.COMPLEX)
        val nonAlcoholicIcon = getStrengthIcon(AlcoholStrength.NON_ALCOHOLIC)
        val strongIcon = getStrengthIcon(AlcoholStrength.STRONG)

        // Speed icon suggests quick/simple
        assertEquals(Icons.Default.Speed, simpleIcon)
        // Engineering icon suggests complexity
        assertEquals(Icons.Default.Engineering, complexIcon)
        // Water drop suggests non-alcoholic
        assertEquals(Icons.Default.WaterDrop, nonAlcoholicIcon)
        // Sports icon suggests strength/intensity
        assertEquals(Icons.Default.Sports, strongIcon)
    }
}

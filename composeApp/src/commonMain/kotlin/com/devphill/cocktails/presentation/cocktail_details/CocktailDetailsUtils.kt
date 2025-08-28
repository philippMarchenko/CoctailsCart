package com.devphill.cocktails.presentation.cocktail_details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.ComplexityLevel

fun getComplexityIcon(complexity: ComplexityLevel): ImageVector {
    return when (complexity) {
        ComplexityLevel.SIMPLE -> Icons.Default.Speed
        ComplexityLevel.MEDIUM -> Icons.Default.Timeline
        ComplexityLevel.COMPLEX -> Icons.Default.Engineering
    }
}

fun getStrengthIcon(strength: AlcoholStrength): ImageVector {
    return when (strength) {
        AlcoholStrength.NON_ALCOHOLIC -> Icons.Default.WaterDrop
        AlcoholStrength.LIGHT -> Icons.Default.LocalBar
        AlcoholStrength.MEDIUM -> Icons.Default.LocalBar
        AlcoholStrength.STRONG -> Icons.Default.Sports
    }
}

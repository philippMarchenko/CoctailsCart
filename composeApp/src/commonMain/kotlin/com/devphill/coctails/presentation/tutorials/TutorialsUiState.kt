package com.devphill.coctails.presentation.tutorials

data class TutorialsUiState(
    val isLoading: Boolean = false,
    val tutorials: List<TutorialSection> = emptyList(),
    val errorMessage: String? = null
)

data class TutorialSection(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val duration: String,
    val steps: List<TutorialStep>
)

data class TutorialStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)


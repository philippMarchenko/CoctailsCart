package com.devphill.coctails.presentation.tutorials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TutorialsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TutorialsUiState())
    val uiState: StateFlow<TutorialsUiState> = _uiState.asStateFlow()

    init {
        loadTutorials()
    }

    private fun loadTutorials() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Mock tutorial data
            val mockTutorials = listOf(
                TutorialSection(
                    id = "1",
                    title = "Basic Bartending Techniques",
                    description = "Learn the fundamentals of mixing cocktails",
                    difficulty = "Beginner",
                    duration = "10 minutes",
                    steps = listOf(
                        TutorialStep(1, "Preparation", "Gather all necessary tools and ingredients"),
                        TutorialStep(2, "Measuring", "Learn proper measuring techniques"),
                        TutorialStep(3, "Mixing", "Master basic mixing methods")
                    )
                ),
                TutorialSection(
                    id = "2",
                    title = "Advanced Cocktail Crafting",
                    description = "Advanced techniques for professional cocktails",
                    difficulty = "Advanced",
                    duration = "20 minutes",
                    steps = listOf(
                        TutorialStep(1, "Layering", "Create beautiful layered cocktails"),
                        TutorialStep(2, "Garnishing", "Professional garnishing techniques"),
                        TutorialStep(3, "Presentation", "Perfect cocktail presentation")
                    )
                )
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                tutorials = mockTutorials
            )
        }
    }
}


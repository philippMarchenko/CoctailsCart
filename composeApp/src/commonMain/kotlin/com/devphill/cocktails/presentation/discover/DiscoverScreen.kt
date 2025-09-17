package com.devphill.cocktails.presentation.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cocktailscart.composeapp.generated.resources.*
import cocktailscart.composeapp.generated.resources.Res
import com.devphill.cocktails.data.platform.NotificationPermissionManager
import com.devphill.cocktails.domain.model.AlcoholStrength
import com.devphill.cocktails.domain.model.Cocktail
import com.devphill.cocktails.domain.model.ComplexityLevel
import com.devphill.cocktails.presentation.common.CocktailImageCard
import com.devphill.cocktails.presentation.common.ErrorMessage
import com.devphill.cocktails.presentation.common.LoadingIndicator
import com.devphill.cocktails.presentation.theme.CocktailScreenTitle
import com.devphill.cocktails.presentation.theme.CocktailSectionHeader
import com.devphill.cocktails.presentation.theme.CocktailSubtitle
import com.devphill.cocktails.presentation.theme.CocktailsTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = koinInject(),
    onCocktailClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationPermissionManager: NotificationPermissionManager = koinInject()

    // Request notification permission when user reaches Discovery screen
    LaunchedEffect(Unit) {
        notificationPermissionManager.requestPermissionIfNeeded()
    }

    DiscoverContent(
        uiState = uiState,
        onRetry = viewModel::retryLoading,
        onCocktailClick = onCocktailClick,
        modifier = modifier,
    )
}

@Composable
internal fun DiscoverContent(
    uiState: DiscoverUiState,
    onRetry: () -> Unit,
    onCocktailClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            uiState.errorMessage != null -> {
                ErrorMessage(
                    message = uiState.errorMessage,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            else -> {
                DiscoverSuccessContent(
                    uiState = uiState,
                    onCocktailClick = onCocktailClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun DiscoverSuccessContent(
    uiState: DiscoverUiState,
    onCocktailClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            WelcomeSection()
        }

        uiState.cocktailOfDay?.let { cocktailOfDay ->
            item {
                CocktailOfDaySection(
                    cocktail = cocktailOfDay,
                    onCocktailClick = onCocktailClick,
                )
            }
        }

        if (uiState.cocktails.isNotEmpty()) {
            item {
                CocktailSectionHeader(
                    text = stringResource(Res.string.all_cocktails),
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            val cocktailRows = uiState.cocktails.chunked(2)
            items(
                items = cocktailRows,
                key = { row -> row.joinToString("-") { it.id } },
            ) { cocktailRow ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    cocktailRow.forEach { cocktail ->
                        CocktailImageCard(
                            cocktail = cocktail,
                            tags =
                                listOf(
                                    cocktail.category,
                                    cocktail.complexity.name,
                                ),
                            onClick = { onCocktailClick(cocktail.id) },
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(0.8f),
                        )
                    }

                    if (cocktailRow.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column {
        CocktailScreenTitle(
            text = stringResource(Res.string.discover),
        )
        CocktailSubtitle(
            text = stringResource(Res.string.find_your_perfect_cocktail),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun CocktailOfDaySection(
    cocktail: Cocktail,
    onCocktailClick: (String) -> Unit = {},
) {
    Column {
        CocktailSectionHeader(
            text = stringResource(Res.string.cocktail_of_day),
            modifier = Modifier.padding(bottom = 12.dp),
        )

        CocktailImageCard(
            cocktail = cocktail,
            tags =
                listOf(
                    cocktail.category,
                    cocktail.complexity.name,
                ),
            onClick = { onCocktailClick(cocktail.id) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f),
        )
    }
}

@Preview()
@Composable
fun DiscoverScreenPreview() {
    // Mock data for preview
    val mockCocktailOfDay =
        Cocktail(
            id = "1",
            title = "Classic Mojito",
            imageUrl = null,
            cocktailUrl = null,
            category = "Classic",
            categoryEnum = "CLASSIC",
            views = "1250",
            ingredients = listOf("White rum", "Fresh lime juice", "Sugar", "Soda water", "Fresh mint"),
            ingredientsEnums = listOf("WHITE_RUM", "LIME_JUICE", "SUGAR", "SODA_WATER", "MINT"),
            method = "Muddle mint and sugar in glass. Add lime juice and rum. Top with soda water.",
            garnish = "Fresh mint sprig",
            glass = "Highball glass",
            videoUrl = null,
            complexity = ComplexityLevel.SIMPLE,
            alcoholStrength = AlcoholStrength.MEDIUM,
            searchText = "mojito classic rum mint lime",
            isFavorite = false,
            preparationTime = 5,
        )

    val mockCocktails =
        listOf(
            Cocktail(
                id = "2",
                title = "Martini",
                imageUrl = null,
                cocktailUrl = null,
                category = "Classic",
                categoryEnum = "CLASSIC",
                views = "980",
                ingredients = listOf("Gin", "Dry vermouth", "Lemon twist"),
                ingredientsEnums = listOf("GIN", "DRY_VERMOUTH", "LEMON"),
                method = "Stir gin and vermouth with ice. Strain into chilled glass.",
                garnish = "Lemon twist",
                glass = "Martini glass",
                videoUrl = null,
                complexity = ComplexityLevel.MEDIUM,
                alcoholStrength = AlcoholStrength.STRONG,
                searchText = "martini classic gin vermouth",
                isFavorite = true,
                preparationTime = 3,
            ),
            Cocktail(
                id = "3",
                title = "Ramos Gin Fizz",
                imageUrl = null,
                cocktailUrl = null,
                category = "Fizz",
                categoryEnum = "FIZZ",
                views = "420",
                ingredients = listOf("Gin", "Lemon juice", "Lime juice", "Sugar", "Cream", "Egg white", "Soda water"),
                ingredientsEnums =
                    listOf(
                        "GIN",
                        "LEMON_JUICE",
                        "LIME_JUICE",
                        "SUGAR",
                        "CREAM",
                        "EGG_WHITE",
                        "SODA_WATER",
                    ),
                method = "Shake all ingredients except soda water for 12 minutes. Add soda water and serve.",
                garnish = "None",
                glass = "Collins glass",
                videoUrl = null,
                complexity = ComplexityLevel.COMPLEX,
                alcoholStrength = AlcoholStrength.MEDIUM,
                searchText = "ramos gin fizz complex cream egg",
                isFavorite = false,
                preparationTime = 15,
            ),
            Cocktail(
                id = "4",
                title = "Virgin Mojito",
                imageUrl = null,
                cocktailUrl = null,
                category = "Mocktail",
                categoryEnum = "MOCKTAIL",
                views = "680",
                ingredients = listOf("Fresh lime juice", "Sugar", "Soda water", "Fresh mint"),
                ingredientsEnums = listOf("LIME_JUICE", "SUGAR", "SODA_WATER", "MINT"),
                method = "Muddle mint and sugar. Add lime juice and top with soda water.",
                garnish = "Fresh mint sprig",
                glass = "Highball glass",
                videoUrl = null,
                complexity = ComplexityLevel.SIMPLE,
                alcoholStrength = AlcoholStrength.NON_ALCOHOLIC,
                searchText = "virgin mojito mocktail mint lime non-alcoholic",
                isFavorite = false,
                preparationTime = 3,
            ),
        )

    val mockUiState =
        DiscoverUiState(
            isLoading = false,
            cocktails = mockCocktails,
            cocktailOfDay = mockCocktailOfDay,
            errorMessage = null,
        )

    CocktailsTheme {
        DiscoverContent(
            uiState = mockUiState,
            onRetry = { },
            onCocktailClick = { },
        )
    }
}

package com.devphill.cocktails.presentation.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import com.devphill.cocktails.presentation.theme.CocktailCardTitle
import com.devphill.cocktails.presentation.theme.CocktailLabel
import com.devphill.cocktails.presentation.theme.CocktailSectionHeader
import com.devphill.cocktails.presentation.theme.CocktailSubtitle
import com.devphill.cocktails.utils.formatToEuropeanDateTime
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailsScreen(
    notificationId: String,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onCocktailClick: (String) -> Unit = {},
    onActionClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val notification = uiState.notifications.find { it.id == notificationId }

    // Mark notification as read when screen opens
    LaunchedEffect(notificationId) {
        if (notification?.isRead == false) {
            viewModel.markAsRead(notificationId)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CocktailSectionHeader("Notification Details")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    // Delete action
                    IconButton(
                        onClick = {
                            notification?.let {
                                viewModel.deleteNotification(it.id)
                                onBackClick()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete notification",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            if (notification != null) {
                NotificationDetailsContent(
                    notification = notification,
                    onCocktailClick = onCocktailClick,
                    onActionClick = onActionClick,
                )
            } else {
                // Notification not found
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CocktailCardTitle("Notification Not Found")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The notification you're looking for might have been deleted or doesn't exist.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBackClick,
                    ) {
                        CocktailLabel("Go Back")
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationDetailsContent(
    notification: Notification,
    onCocktailClick: (String) -> Unit,
    onActionClick: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Notification Header
        NotificationHeader(notification = notification)

        // Divider
        HorizontalDivider()

        // Notification Content
        NotificationContent(notification = notification)

        // Action Buttons (if applicable)
        NotificationActions(
            notification = notification,
            onCocktailClick = onCocktailClick,
            onActionClick = onActionClick,
        )
    }
}

@Composable
private fun NotificationHeader(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = getNotificationBackgroundColor(notification.type),
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Notification type icon
            Icon(
                imageVector = getNotificationIcon(notification.type),
                contentDescription = null,
                tint = getNotificationColor(notification.type),
                modifier = Modifier.size(32.dp),
            )

            Column(
                modifier = Modifier.weight(1f),
            ) {
                CocktailCardTitle(notification.title)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.timestamp.formatToEuropeanDateTime(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = getNotificationTypeLabel(notification.type),
                    style = MaterialTheme.typography.labelSmall,
                    color = getNotificationColor(notification.type),
                )
            }
        }
    }
}

@Composable
private fun NotificationContent(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            CocktailSubtitle("Message")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun NotificationActions(
    notification: Notification,
    onCocktailClick: (String) -> Unit,
    onActionClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Cocktail action button
        notification.cocktailId?.let { cocktailId ->
            Button(
                onClick = { onCocktailClick(cocktailId) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.LocalBar,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                CocktailLabel("View Cocktail")
            }
        }

        // Custom action button
        notification.actionUrl?.let { actionUrl ->
            OutlinedButton(
                onClick = { onActionClick(actionUrl) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                CocktailLabel("Open Link")
            }
        }
    }
}

// Helper functions for notification styling
@Composable
private fun getNotificationIcon(type: NotificationType) =
    when (type) {
        NotificationType.NEW_COCKTAIL -> Icons.Default.LocalBar
        NotificationType.FAVORITE_UPDATE -> Icons.Default.Star
        NotificationType.SYSTEM_MESSAGE -> Icons.Default.Settings
        NotificationType.PROMOTION -> Icons.Default.LocalOffer
        NotificationType.REMINDER -> Icons.Default.NotificationImportant
    }

@Composable
private fun getNotificationColor(type: NotificationType) =
    when (type) {
        NotificationType.NEW_COCKTAIL -> MaterialTheme.colorScheme.primary
        NotificationType.FAVORITE_UPDATE -> MaterialTheme.colorScheme.secondary
        NotificationType.SYSTEM_MESSAGE -> MaterialTheme.colorScheme.tertiary
        NotificationType.PROMOTION -> MaterialTheme.colorScheme.error
        NotificationType.REMINDER -> MaterialTheme.colorScheme.outline
    }

@Composable
private fun getNotificationBackgroundColor(type: NotificationType) =
    when (type) {
        NotificationType.NEW_COCKTAIL -> MaterialTheme.colorScheme.primaryContainer
        NotificationType.FAVORITE_UPDATE -> MaterialTheme.colorScheme.secondaryContainer
        NotificationType.SYSTEM_MESSAGE -> MaterialTheme.colorScheme.tertiaryContainer
        NotificationType.PROMOTION -> MaterialTheme.colorScheme.errorContainer
        NotificationType.REMINDER -> MaterialTheme.colorScheme.surfaceVariant
    }

private fun getNotificationTypeLabel(type: NotificationType) =
    when (type) {
        NotificationType.NEW_COCKTAIL -> "New Cocktail"
        NotificationType.FAVORITE_UPDATE -> "Favorite Update"
        NotificationType.SYSTEM_MESSAGE -> "System Message"
        NotificationType.PROMOTION -> "Promotion"
        NotificationType.REMINDER -> "Reminder"
    }

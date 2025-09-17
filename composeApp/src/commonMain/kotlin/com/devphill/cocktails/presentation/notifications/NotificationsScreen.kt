package com.devphill.cocktails.presentation.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import com.devphill.cocktails.presentation.theme.CenteredTitle
import com.devphill.cocktails.presentation.theme.CocktailBodyText
import com.devphill.cocktails.presentation.theme.CocktailCardTitle
import com.devphill.cocktails.presentation.theme.CocktailLabel
import com.devphill.cocktails.presentation.theme.CocktailSectionHeader
import com.devphill.cocktails.presentation.theme.CocktailSubtitle
import com.devphill.cocktails.presentation.theme.ErrorText
import com.devphill.cocktails.utils.formatToEuropeanDateTime
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNotificationClick: (Notification) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CocktailSectionHeader("Notifications")
                        if (uiState.unreadCount > 0) {
                            Badge {
                                CocktailLabel(uiState.unreadCount.toString())
                            }
                        }
                    }
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
                    if (uiState.unreadCount > 0) {
                        TextButton(
                            onClick = { viewModel.markAllAsRead() },
                        ) {
                            CocktailBodyText("Mark all read")
                        }
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
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    ErrorState(
                        error = uiState.error,
                        onRetry = { /* Implement retry logic */ },
                    )
                }

                uiState.notifications.isEmpty() -> {
                    EmptyNotificationsState()
                }

                else -> {
                    NotificationsList(
                        notifications = uiState.notifications,
                        onNotificationClick = { notification ->
                            if (!notification.isRead) {
                                viewModel.markAsRead(notification.id)
                            }
                            onNotificationClick(notification)
                        },
                        onDeleteClick = { notification ->
                            viewModel.deleteNotification(notification.id)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationsList(
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit,
    onDeleteClick: (Notification) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = notifications,
            key = { it.id },
        ) { notification ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
            ) {
                NotificationItem(
                    notification = notification,
                    onClick = { onNotificationClick(notification) },
                    onDeleteClick = { onDeleteClick(notification) },
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (notification.isRead) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Notification type icon
            Icon(
                imageVector = getNotificationIcon(notification.type),
                contentDescription = null,
                tint = getNotificationColor(notification.type),
                modifier = Modifier.size(24.dp),
            )

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    CocktailCardTitle(
                        text = notification.title,
                        modifier = Modifier.weight(1f),
                    )

                    if (!notification.isRead) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primary,
                            modifier =
                                Modifier
                                    .size(8.dp),
                        ) {}
                    }
                }

                CocktailBodyText(
                    text = notification.message,
                )

                CocktailLabel(
                    text = notification.timestamp.formatToEuropeanDateTime(),
                )
            }

            // Delete button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete notification",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyNotificationsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline,
            )
            CenteredTitle(
                text = "No notifications yet",
            )
            CocktailSubtitle(
                text = "We'll notify you when there's something new!",
            )
        }
    }
}

@Composable
private fun ErrorState(
    error: String?,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )
            CenteredTitle(
                text = "Something went wrong",
            )
            ErrorText(
                text = error ?: "Unable to load notifications.",
            )
            Button(onClick = onRetry) {
                CocktailBodyText("Try again")
            }
        }
    }
}

@Composable
private fun getNotificationIcon(type: NotificationType) =
    when (type) {
        NotificationType.NEW_COCKTAIL -> Icons.Default.LocalBar
        NotificationType.FAVORITE_UPDATE -> Icons.Default.Favorite
        NotificationType.SYSTEM_MESSAGE -> Icons.Default.Info
        NotificationType.PROMOTION -> Icons.Default.LocalOffer
        NotificationType.REMINDER -> Icons.Default.Schedule
    }

@Composable
private fun getNotificationColor(type: NotificationType) =
    when (type) {
        NotificationType.NEW_COCKTAIL -> MaterialTheme.colorScheme.primary
        NotificationType.FAVORITE_UPDATE -> MaterialTheme.colorScheme.error
        NotificationType.SYSTEM_MESSAGE -> MaterialTheme.colorScheme.tertiary
        NotificationType.PROMOTION -> MaterialTheme.colorScheme.secondary
        NotificationType.REMINDER -> MaterialTheme.colorScheme.outline
    }

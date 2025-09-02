package com.devphill.cocktails.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devphill.cocktails.domain.model.Notification
import com.devphill.cocktails.domain.model.NotificationType
import com.devphill.cocktails.domain.interactor.NotificationInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = true,
    val unreadCount: Int = 0,
    val error: String? = null
)

class NotificationsViewModel(
    private val notificationInteractor: NotificationInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                notificationInteractor.getAllNotifications().collectLatest { notifications ->
                    val unreadCount = notificationInteractor.getUnreadCount()
                    _uiState.value = _uiState.value.copy(
                        notifications = notifications, // Already sorted in interactor
                        isLoading = false,
                        unreadCount = unreadCount,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                notificationInteractor.markNotificationAsRead(notificationId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to mark notification as read"
                )
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                notificationInteractor.markAllNotificationsAsRead()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to mark all notifications as read"
                )
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            try {
                notificationInteractor.deleteNotification(notificationId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete notification"
                )
            }
        }
    }

    fun deleteAllReadNotifications() {
        viewModelScope.launch {
            try {
                notificationInteractor.deleteAllReadNotifications()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete read notifications"
                )
            }
        }
    }

    fun loadUnreadNotifications() {
        viewModelScope.launch {
            try {
                notificationInteractor.getUnreadNotifications().collectLatest { notifications ->
                    _uiState.value = _uiState.value.copy(
                        notifications = notifications,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load unread notifications"
                )
            }
        }
    }

    fun loadNotificationsByType(type: NotificationType) {
        viewModelScope.launch {
            try {
                notificationInteractor.getNotificationsByType(type).collectLatest { notifications ->
                    _uiState.value = _uiState.value.copy(
                        notifications = notifications,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load notifications by type"
                )
            }
        }
    }

    fun createNotification(
        title: String,
        message: String,
        type: NotificationType,
        cocktailId: String? = null,
        actionUrl: String? = null
    ) {
        viewModelScope.launch {
            try {
                notificationInteractor.createNotification(title, message, type, cocktailId, actionUrl)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to create notification"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

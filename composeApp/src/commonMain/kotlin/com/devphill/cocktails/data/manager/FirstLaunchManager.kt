package com.devphill.cocktails.data.manager

interface FirstLaunchManager {
    suspend fun handleFirstLaunch()
    suspend fun isFirstLaunch(): Boolean
    suspend fun markAsLaunched()
}
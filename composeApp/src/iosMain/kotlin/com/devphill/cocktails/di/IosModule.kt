package com.devphill.cocktails.di

import com.devphill.cocktails.data.platform.PushNotificationManager
import com.devphill.cocktails.data.platform.PushNotificationManagerImpl
import org.koin.dsl.module

val iosModule = module {
    single<PushNotificationManager> { PushNotificationManagerImpl() }
}

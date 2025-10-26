package com.devphill.cocktails.presentation.profile

import com.devphill.cocktails.data.platform.ShareManager

class FakeShareManager : ShareManager {
    override fun shareText(text: String, subject: String?) { /* no-op for test */ }
    override fun shareApp(appName: String, appUrl: String) { /* no-op for test */ }
}


package com.devphill.cocktails

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
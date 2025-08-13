package com.devphill.coctails

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
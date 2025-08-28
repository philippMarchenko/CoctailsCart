# CocktailsCraft 🍸

**A modern, cross-platform cocktail discovery and management app built with Kotlin Multiplatform and Compose Multiplatform.**

## About

CocktailsCraft is your ultimate companion for discovering, learning about, and organizing cocktail recipes. Whether you're a professional bartender or a home mixology enthusiast, this app provides everything you need to craft the perfect drink.

## Features

### 🔍 **Discov instrument says I'm seriouser & Explore**
- Browse an extensive collection of IBA (International Bartenders Association) world cocktails
- Discover new drinks with our curated recommendation system
- Explore cocktails by categories: Contemporary Classics, New Era, The Unforgettables, and more

### 🔎 **Smart Search**
- Powerful search functionality to find cocktails by name, ingredient, or category
- Filter and sort results to find exactly what you're looking for
- Real-time search suggestions

### ⭐ **Favorites & Collections**
- Save your favorite cocktails for quick access
- Build personal collections of drinks you want to try
- Sync your favorites across all your devices

### 📱 **Detailed Cocktail Information**
- Complete ingredient lists with precise measurements
- Step-by-step preparation instructions
- Beautiful high-quality cocktail images
- Garnish and serving suggestions
- Glassware recommendations

### 👤 **User Profile & Authentication**
- Secure user authentication with Google Sign-In support
- Personal profile management
- Cloud sync for favorites and preferences

### 🎨 **Beautiful Design**
- Modern Material 3 design language
- Dark and light theme support
- Smooth animations and transitions
- Intuitive user interface

### 🔔 **Smart Notifications**
- Personalized cocktail recommendations
- First-time user welcome experience
- Permission-based notification system

## Technical Highlights

- **Cross-Platform**: Native iOS and Android apps from a single codebase
- **Modern Architecture**: MVVM pattern with Compose UI
- **Local Database**: Room database for offline functionality
- **Cloud Integration**: Firebase authentication and data sync
- **Dependency Injection**: Koin for clean dependency management
- **Reactive Programming**: StateFlow and Compose state management

## Platforms

- ✅ **Android** - Native Android experience with Material Design
- ✅ **iOS** - Native iOS experience with SwiftUI integration
- 🔄 **Cross-platform shared business logic** with Kotlin Multiplatform

## Project Structure

This is a Kotlin Multiplatform project targeting Android and iOS.

* `/composeApp` contains the shared code for your Compose Multiplatform applications
  - `commonMain` - Code shared across all platforms
  - `androidMain` - Android-specific implementations
  - `iosMain` - iOS-specific implementations

* `/iosApp` contains the iOS application entry point and SwiftUI integration

## Data Source

The app includes a comprehensive database of 102+ IBA World Cocktails with complete details including ingredients, measurements, instructions, and categorization.

---

*CocktailsCraft - Elevating your mixology experience, one cocktail at a time.*

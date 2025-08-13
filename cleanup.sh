#!/bin/bash

# Clean script for CoctailsCart project
# Removes unnecessary build files and caches

echo "🧹 Cleaning CoctailsCart project..."

# Remove build directories
echo "Removing build directories..."
rm -rf build/
rm -rf composeApp/build/
rm -rf .gradle/
rm -rf .kotlin/

# Remove IDE and system files
echo "Removing system files..."
find . -name ".DS_Store" -delete
find . -name "*.tmp" -delete

# Remove any log files
find . -name "*.log" -delete

echo "✅ Cleanup complete!"
echo ""
echo "Essential files kept:"
echo "📱 Android & iOS source code"
echo "⚙️  Build configuration files (build.gradle.kts, etc.)"
echo "📦 Gradle wrapper files"
echo "🔧 Project settings"
echo ""
echo "ℹ️  Note: Generated resources folder will be recreated on next build"
echo "   (currently unused in your cocktails app)"

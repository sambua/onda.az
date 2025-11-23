# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Kotlin Coding Guidelines

### Nomenclature

- Use PascalCase for classes and files (e.g., `AuthScreen.kt`, `CustomerRepository.kt`)
- Use camelCase for variables, functions, and methods
- Use SCREAMING_SNAKE_CASE for constants
- Start functions with a verb (e.g., `createCustomer`, `signOut`)
- Use boolean prefixes: `isLoading`, `hasError`, `canDelete`
- Avoid abbreviations except standard ones (API, URL, ID)

### Functions

- Write short functions with a single purpose (<20 lines)
- Avoid nesting with early returns and guard clauses
- Use higher-order functions (map, filter, fold) over loops
- Use lambdas for simple operations, named functions for complex ones
- Use default parameter values instead of null checks

### Data & Classes

- Use `data class` for data containers
- Prefer `val` over `var` for immutability
- Follow SOLID principles
- Prefer composition over inheritance
- Define interfaces for contracts (e.g., `CustomerRepository`)

### Testing

- Follow Arrange-Act-Assert convention
- Name variables: `inputX`, `mockX`, `actualX`, `expectedX`
- Use test doubles for dependencies

## Project Overview

Onda is a Kotlin Multiplatform mobile application built for Android and iOS using Compose Multiplatform. The project follows Clean Architecture principles with a feature-based modular structure.

## Common Development Commands

```bash
# Build entire project
./gradlew build

# Build Android debug APK
./gradlew :composeApp:assembleDebug

# Windows: use gradlew.bat instead
# .\gradlew.bat :composeApp:assembleDebug

# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :feature:auth:test

# Generate SHA-1 fingerprint for Google Sign-In
./gradlew signingReport

# Build iOS - open in Xcode
open iosApp/iosApp.xcodeproj
```

## Module Structure

```
onda.az/
├── composeApp/     # App entry point (Android: MainActivity, iOS: MainViewController)
├── data/           # Repository implementations (CustomerRepository)
├── di/             # Koin dependency injection setup
├── navigation/     # Type-safe navigation graph with @Serializable routes
├── shared/         # Colors, Fonts, Domain models, RequestState, Constants
└── feature/
    ├── auth/       # Google Sign-In authentication (AuthScreen, AuthViewModel)
    ├── home/       # Home screen with drawer & bottom nav (HomeGraphScreen)
    └── profile/    # User profile (CustomTextField component only)
```

## Architecture

**MVVM + Clean Architecture** with Repository pattern:
- ViewModels injected via Koin (`viewModelOf()`)
- `CustomerRepository` interface abstracts data access
- `RequestState<T>` sealed class for async state (Idle/Loading/Success/Error)

**Type-safe Navigation**: Routes defined as `@Serializable` objects in `shared/.../Screen.kt`. Navigation graph in `navigation/SetupNavGraph.kt`.

**Package convention**: `az.onda.{module}` (e.g., `az.onda.auth`, `az.onda.home`)

## Key Files

- `shared/.../Constants.kt` - OAuth client IDs (Web/Android and iOS)
- `di/.../KoinModule.kt` - All DI wiring
- `data/.../CustomerRepositoryImpl.kt` - Data layer (needs backend implementation)
- `composeApp/.../App.kt` - Root composable, initializes GoogleAuthProvider

## iOS Setup

Google Sign-In iOS package must be added manually:
1. Open Xcode > File > Add Packages...
2. Add URL: https://github.com/google/GoogleSignIn-iOS

## Technology Stack

- Kotlin 2.2.20, Compose Multiplatform 1.9.1
- Koin 4.1.1, Navigation Compose 2.9.1
- KMPAuth 2.3.1 (Google Sign-In)
- Coil 3.3.0, Kotlinx Serialization 1.9.0

Dependencies managed in `gradle/libs.versions.toml`.

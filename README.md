# 📰 News App

A simple modern Android News App built using **Jetpack Compose**, **Koin**, **Retrofit**, and **Room DB** following **Clean Architecture**.

---

## 🚀 Tech Stack
- Kotlin
- Jetpack Compose (UI)
- Hilt (Dependency Injection)
- Retrofit (Networking)
- Room Database (Local Storage)
- MVVM + Clean Architecture
- Coroutines & Flow (for async operations)
- Material 3 (for modern UI components)

---

## 📱 Screenshots
_(Adding screenshots once UI is ready!)_

---

## 📦 Features
- Fetch latest news from public API
- List of articles with title, description, image
- Pull to refresh
- View article details
- Local caching using Room DB (optional enhancement)
- Error handling and loading states
- Category filtering (optional enhancement)

---

## 🛠 Architecture

com.yourcompany.newsapp/
├── data/
│   ├── local/             // Room Database related classes
│   │   ├── dao/           // Data Access Objects (DAOs)
│   │   ├── database/      // Database class definition
│   │   └── entity/        // Room Entities
│   ├── remote/            // Retrofit API service related classes
│   │   ├── api/           // Retrofit Service Interface
│   │   └── dto/           // Data Transfer Objects (DTOs) - models from API
│   └── repository/        // Implementations of Domain layer repositories
│       └── NewsRepositoryImpl.kt
├── domain/
│   ├── model/             // Domain models (business objects, ideally plain Kotlin data classes)
│   │   └── Article.kt     // Our core Article model
│   ├── repository/        // Interfaces for repositories (defined in domain, implemented in data)
│   │   └── NewsRepository.kt
│   └── usecase/           // Business logic operations (Use Cases)
│       ├── GetNewsUseCase.kt
│       └── SaveArticleUseCase.kt
│       └── GetSavedArticlesUseCase.kt
│       └── ...
├── presentation/
│   ├── ui/                // Jetpack Compose UI screens and components
│   │   ├── news_list/     // Screen and components for news list
│   │   │   └── NewsListScreen.kt
│   │   ├── news_detail/   // Screen and components for article detail
│   │   │   └── NewsDetailScreen.kt
│   │   ├── saved_news/    // Screen and components for saved articles
│   │   │   └── SavedNewsScreen.kt
│   │   └── components/    // Reusable UI components (e.g., ArticleListItem.kt)
│   └── viewmodel/         // ViewModels for UI screens
│       ├── NewsListViewModel.kt
│       ├── NewsDetailViewModel.kt
│       └── SavedNewsViewModel.kt
├── di/                    // Hilt Dependency Injection modules
│   ├── AppModule.kt       // Application-wide dependencies
│   └── NetworkModule.kt   // Networking specific dependencies
│   └── DatabaseModule.kt  // Database specific dependencies
├── util/                  // Utility classes (e.g., Constants, Resource wrapper)
└── MainActivity.kt        // Entry point (mostly handles navigation setup)
└── NewsApplication.kt     // Hilt Application class
└── ui/theme/              // Compose theme files (already generated)


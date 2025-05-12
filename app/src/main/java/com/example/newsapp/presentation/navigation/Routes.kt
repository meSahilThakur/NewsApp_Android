package com.example.newsapp.presentation.navigation

import kotlinx.serialization.Serializable
import retrofit2.http.Url

sealed class Routes {

    @Serializable
    object NewsScreen: Routes()             // Using object for destinations with no arguments

    @Serializable
    data class NewsDetailScreen(            // For destinations with arguments, we use a data class
        val articleUrl: String
    ): Routes()

    @Serializable
    object SavedArticlesScreen: Routes()

    @Serializable
    object SearchScreen: Routes()

}
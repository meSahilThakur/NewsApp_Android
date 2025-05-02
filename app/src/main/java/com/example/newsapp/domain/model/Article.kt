package com.example.newsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val sourceName: String?, // Using sourceName directly
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val isSaved: Boolean = false // Added a field for the saved state in the UI/Domain
): Parcelable       // Implement Parcelable
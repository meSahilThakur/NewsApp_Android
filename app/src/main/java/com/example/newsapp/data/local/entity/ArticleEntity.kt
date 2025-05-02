package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")     // Defining table name
data class ArticleEntity(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val sourceName: String?,
    val title: String?,
    // Using the URL as the primary key since it's unique for each article
    @PrimaryKey
    val url: String,
    val urlToImage: String?,
    val timestamp: Long = System.currentTimeMillis() // Optional: add timestamp for caching
)

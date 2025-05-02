package com.example.newsapp.data.remote.dto

data class ArticleDto(      // ArticleDto: Represents a news article with details like title, description, URL, and image.
    val source: SourceDto?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

data class SourceDto(       // SourceDto: Information about the source of the article (e.g., BBC, CNN).
    val id: String?,
    val name: String?
)
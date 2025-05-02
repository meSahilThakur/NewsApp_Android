package com.example.newsapp.data.remote.dto

data class NewsResponse(        // NewsResponse: The top-level response object.
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)


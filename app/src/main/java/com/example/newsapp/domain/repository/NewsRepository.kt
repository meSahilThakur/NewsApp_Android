package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<List<Article>>                  // Get a flow of articles (potentially from cache, then network)
    suspend fun saveArticle(article: Article)           // Save an article to local storage
    suspend fun deleteArticle(article: Article)         // Delete an article from local storage
    fun getSavedArticles(): Flow<List<Article>>          // Get a flow of saved articles from local storage
    suspend fun isArticleSaved(articleUrl: String): Boolean     // Check if an article is saved (e.g., for UI state)

}
package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(query: String? = null): Flow<Resource<List<Article>>>                  // Get a flow of articles (potentially from cache, then network)
    suspend fun saveArticle(article: Article)           // Save an article to local storage
    suspend fun deleteArticle(article: Article)         // Delete an article from local storage
    fun getSavedArticles(): Flow<List<Article>>          // Get a flow of saved articles from local storage

    fun getArticleByUrl(articleUrl: String): Flow<Resource<Article>>       // Get single article by url, Used in detail screen
    suspend fun isArticleSaved(articleUrl: String): Boolean     // Check if an article is saved (e.g., for UI state)



//    fun getNews(query: String? = null): Flow<Resource<List<Article>>>
//    fun getSavedArticles(): Flow<List<Article>>
//    suspend fun upsertArticle(article: Article)
//    suspend fun deleteArticle(article: Article)
//    suspend fun checkIfArticleIsSaved(articleUrl: String): Boolean

}
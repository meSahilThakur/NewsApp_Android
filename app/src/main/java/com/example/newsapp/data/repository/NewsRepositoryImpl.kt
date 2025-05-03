package com.example.newsapp.data.repository

import android.util.Log
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.mapper.ArticleMapper.toArticle
import com.example.newsapp.data.mapper.ArticleMapper.toArticleEntity
import com.example.newsapp.data.remote.api.NewsApi
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(   // Hilt will inject these dependencies
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
): NewsRepository {
    override fun getNews(): Flow<Resource<List<Article>>> = flow{
        emit(Resource.Loading)
        try {
            val response = newsApi.getTopHeadlines()
            val articles = response.articles.map { it.toArticle() }
            emit(Resource.Success(articles))
        }catch (e : Exception){
            Log.e("NewsRepositoryImpl", "General Exception fetching news: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Something went wrong"))
        }
    }

    override suspend fun saveArticle(article: Article) {
        // Convert domain model to Room entity
        val entity = article.toArticleEntity()
        articleDao.insertArticle(entity)
    }


    override suspend fun deleteArticle(article: Article) {
        val articleUrl = article.url
        if (articleUrl!= null){
            articleDao.deleteArticle(article.url)
        }else{
            // Optionally log or handle the case where an article without a URL is attempted to be deleted
            Log.e("NewsRepositoryImpl", "Attempted to delete article with null URL: ${article.title}")
            // We simply do nothing if the URL is null, as we can't delete from DB by URL
        }
    }


    override fun getSavedArticles(): Flow<List<Article>> {
        // Get flow of entities from DB and map to domain models
        return articleDao.getAllArticles().map { entities ->
            entities.map { it.toArticle() } // Convert each entity to domain model
        }
    }

    override suspend fun isArticleSaved(articleUrl: String): Boolean {
        return articleDao.isArticleSaved(articleUrl)
    }
}
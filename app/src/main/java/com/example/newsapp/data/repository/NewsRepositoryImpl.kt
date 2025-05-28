package com.example.newsapp.data.repository

import android.util.Log
import android.util.Log.e
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.mapper.ArticleMapper.toArticle
import com.example.newsapp.data.mapper.ArticleMapper.toArticleEntity
import com.example.newsapp.data.remote.api.NewsApi
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(   // Hilt will inject these dependencies
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
): NewsRepository {
    override fun getNews(query: String?): Flow<Resource<List<Article>>> = flow{
        emit(Resource.Loading)
        try {
            val response = if (query.isNullOrBlank()) {
                newsApi.getTopHeadlines()
            }else{
                newsApi.searchNews(query = query)
            }
            val articles = response.articles.map { it.toArticle() }
            emit(Resource.Success(articles))
        }catch (e : Exception){
            Log.e("NewsRepositoryImpl", "General Exception fetching news: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Something went wrong"))
        }
    }

    override suspend fun saveArticle(article: Article) {
        // Convert domain model to Room entity
        val entity = article.toArticleEntity().copy(isSaved = true)
        Log.d("SaveArticleRepo", "Saving entity: $entity")
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
        // Get flow of saved entities from DB and map to domain models
        return articleDao.getSavedArticles().map { entities -> // Use the new getSavedArticles() DAO function
            entities.map { it.toArticle() } // Convert each entity to domain model
        }
    }


    override fun getArticleByUrl(articleUrl: String): Flow<Resource<Article>> = flow{
        emit(Resource.Loading)
        try {
            val response = newsApi.getTopHeadlines()
            val articleDto = response.articles.find { it.url == articleUrl }                    // Finds the specific article DTO by URL
            if (articleDto != null){
                val article = articleDto.toArticle()
                emit(Resource.Success(article))
            }else{
                emit(Resource.Error("Article details not found online."))
            }
        } catch (e: HttpException) {                                                        //jab network request server tak pahunch jaaye, par server kuch error code return kare (jaise 404 Not Found, 500 Internal Server Error)
            val errorMessage = e.localizedMessage ?: "An unexpected HTTP error occurred fetching article details"
            Log.e("NewsRepositoryImpl", "HTTP Exception fetching article: $errorMessage", e)
            emit(Resource.Error(errorMessage))

        } catch (e: IOException) {                                                          //jab network connection mein hi koi problem ho. Jaise phone mein internet na ho, server down ho, ya request timeout ho jaaye. Yahan request server tak theek se pahunch hi nahi paayi.
            val errorMessage = e.localizedMessage ?: "Couldn't reach server to get article details. Check your internet connection."
            Log.e("NewsRepositoryImpl", "IO Exception fetching article: $errorMessage", e)
            emit(Resource.Error(errorMessage))

        } catch (e: Exception) {                                                            //kisi bhi doosre tarah ke exception ko handle karega, jo na toh HttpException hai aur na IOException. Jaise aapke try block ke andar koi null pointer exception aa jaaye, ya JSON parsing mein koi unexpected error ho jaaye (agar converter mein problem ho), ya koi aur logic error.
            val errorMessage = e.localizedMessage ?: "An unknown error occurred fetching article details"
            Log.e("NewsRepositoryImpl", "General Exception fetching article: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }
    }

    override suspend fun isArticleSaved(articleUrl: String): Boolean {
        return articleDao.isArticleSaved(articleUrl)
    }

}
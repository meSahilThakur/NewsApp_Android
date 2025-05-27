package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao // Annotate as a Room DAO
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignore if article with same URL exists
    suspend fun insertArticle(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE url = :articleUrl")
    suspend fun deleteArticle(articleUrl: String)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<ArticleEntity>> // Return Flow to observe changes

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE url = :articleUrl LIMIT 1)")
    suspend fun isArticleSaved(articleUrl: String): Boolean

    @Query("SELECT * FROM articles WHERE isSaved = 1")
    fun getSavedArticles(): Flow<List<ArticleEntity>>
}
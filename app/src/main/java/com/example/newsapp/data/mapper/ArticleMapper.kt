package com.example.newsapp.data.mapper

import com.example.newsapp.data.local.entity.ArticleEntity
import com.example.newsapp.data.remote.dto.ArticleDto
import com.example.newsapp.domain.model.Article

object ArticleMapper {      // Use an object for simple stateless mapper functions
    fun Article.toArticleEntity(): ArticleEntity{
        return ArticleEntity(           // Mapper from Domain Model to Entity (for saving to DB)
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            sourceName = sourceName,
            title = title,
            url = url ?: throw IllegalArgumentException("Article URL cannot be null for database"),
            urlToImage = urlToImage
        )
    }

    // Mapper from Entity to Domain Model (for reading from DB)
    fun ArticleEntity.toArticle(): Article {
        return Article(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            sourceName = sourceName,
            title = title,
            url = url,
            urlToImage = urlToImage,
            isSaved = true // If it's in the DB, it's saved
        )
    }

    // Mapper from DTO to Domain Model (for reading from API)
    fun ArticleDto.toArticle(): Article {
        return Article(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            sourceName = source?.name,
            title = title,
            url = url,
            urlToImage = urlToImage,
            isSaved = false // Assume fetched articles are not saved by default
        )
    }
}
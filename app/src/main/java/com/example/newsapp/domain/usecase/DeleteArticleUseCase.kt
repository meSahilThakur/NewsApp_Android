package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteArticleUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(article: Article){
        repository.deleteArticle(article)           // Repository implementation already handles null URL check
    }
}
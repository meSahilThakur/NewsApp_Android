package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveArticleUseCase @Inject constructor( private val repository: NewsRepository) {
    suspend operator fun invoke(article: Article){          // This UseCase takes an Article and performs a suspend function
        repository.saveArticle(article)
    }
}
package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor( private val repository: NewsRepository) {
    operator fun invoke(): Flow<List<Article>>{
        return repository.getSavedArticles()        // Repository implementation already maps Entity Flow to Domain Model Flow
    }
}
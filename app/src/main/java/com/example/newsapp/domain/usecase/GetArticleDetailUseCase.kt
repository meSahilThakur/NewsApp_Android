package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticleDetailUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(articleUrl: String): Flow<Resource<Article>>{
        return repository.getArticleByUrl(articleUrl = articleUrl)
    }
}
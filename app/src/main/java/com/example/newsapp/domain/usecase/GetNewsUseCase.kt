package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String? = null): Flow<Resource<List<Article>>> =
        repository.getNews(query)
            .onStart { emit(Resource.Loading) }
            .catch { error ->
                emit(Resource.Error(error.localizedMessage ?: "An unexpected error occurred"))
            }
}
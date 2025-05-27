package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor( private val repository: NewsRepository) {
    operator fun invoke(): Flow<Resource<List<Article>>> = repository.getSavedArticles()
            .map { Resource.Success(it) }
//            .onStart { emit(Resource.Loading) }
//            .catch { error -> emit(Resource.Error(error.localizedMessage ?: "An unexpected error occurred")) }



//    operator fun invoke(): Flow<Resource<List<Article>>>{
//        return repository.getSavedArticles()        // Repository implementation already maps Entity Flow to Domain Model Flow
//    }

}
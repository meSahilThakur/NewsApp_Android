package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class IsArticleSavedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(articleUrl: String): Boolean{
        return repository.isArticleSaved(articleUrl)
    }
}
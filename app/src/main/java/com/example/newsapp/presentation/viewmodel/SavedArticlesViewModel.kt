package com.example.newsapp.presentation.viewmodel

import android.R.attr.resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.usecase.GetSavedArticlesUseCase
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedArticlesViewModel @Inject constructor(
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase
): ViewModel() {
    private val _savedArticleState = MutableStateFlow(GetSavedArticlesState())
    val savedArticlesState = _savedArticleState.asStateFlow()

    init {
        getSavedArticles()
    }

    fun getSavedArticles(){
        Log.d("SavedArticlesVM", "getSavedArticles() called")
        viewModelScope.launch {
            getSavedArticlesUseCase().collectLatest {
//                Log.d("SavedArticlesVM", "Resource received: $resource, Class: ${resource::class.java.name}")
//                val successResource = resource as? Resource.Success<List<Article>>
//                if (resource is Resource.Loading) {
//                    Log.d("SavedArticlesVM", "State set to loading (IF)")
//                    _savedArticleState.value = GetSavedArticlesState(isLoading = true)
//                } else if (successResource != null) {
//                    Log.d("SavedArticlesVM", "Inside Resource.Success block (IF - Casted)")
//                    Log.d("SavedArticlesVM", "Resource received (IF - Casted): ${successResource.data.size} articles")
//                    Log.d("SavedArticlesVM", "Data to set in state (IF - Casted): ${successResource.data}")
//                    _savedArticleState.value = GetSavedArticlesState(data = successResource.data)
//                    Log.d("SavedArticlesVM", "State updated with ${successResource.data.size} articles (IF - Casted)")
//                } else if (resource is Resource.Error) {
//                    Log.d("SavedArticlesVM", "State set to error (IF)")
//                    _savedArticleState.value = GetSavedArticlesState(error = resource.error)
//                }
//                Log.d("SavedArticlesVM", "After if-else statement")

                when(it){
                    is Resource.Loading -> _savedArticleState.value = GetSavedArticlesState(isLoading = true)
                    is Resource.Success -> _savedArticleState.value = GetSavedArticlesState(data = it.data)
                    is Resource.Error -> _savedArticleState.value = GetSavedArticlesState(error = it.error)
                }
            }
        }
    }

}

data class GetSavedArticlesState(
    val isLoading: Boolean = false,
    val data: List<Article> = emptyList(),
    val error: String = ""
)
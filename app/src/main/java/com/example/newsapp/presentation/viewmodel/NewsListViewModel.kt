package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.usecase.DeleteArticleUseCase
import com.example.newsapp.domain.usecase.GetNewsUseCase
import com.example.newsapp.domain.usecase.SaveArticleUseCase
import com.example.newsapp.presentation.common.DeleteArticleState
import com.example.newsapp.presentation.common.SaveArticleState
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow(GetNewsState())
    val newsState = _newsState.asStateFlow()

    private val _saveArticleState = MutableStateFlow(SaveArticleState())
    val saveArticleState = _saveArticleState.asStateFlow()

    private val _deleteArticleState = MutableStateFlow(DeleteArticleState())
    val deleteArticleState = _deleteArticleState.asStateFlow()

    init {
        getNews()
    }

    fun getNews(){
        viewModelScope.launch {
            getNewsUseCase().collectLatest {
                when(it){
                    is Resource.Loading ->  _newsState.value = GetNewsState(isLoading = true)
                    is Resource.Success -> _newsState.value = GetNewsState(getNewsData = it.data)
                    is Resource.Error -> _newsState.value = GetNewsState(error = it.error)
                }
            }
        }
    }

    fun saveArticle(article: Article){
        viewModelScope.launch {
            _saveArticleState.value = SaveArticleState(isLoading = true)
            try {
                saveArticleUseCase(article)
                _saveArticleState.value = SaveArticleState(isSuccess = true)
            }catch (e: Exception){
                _saveArticleState.value = SaveArticleState(error = e.localizedMessage ?: "Unknown Error")
            }
        }
    }
    fun resetSaveArticleState() {
        _saveArticleState.value = SaveArticleState()
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            _deleteArticleState.value = DeleteArticleState(isLoading = true)
            try {
                deleteArticleUseCase(article)
                _deleteArticleState.value = DeleteArticleState(isSuccess = true)
            } catch (e: Exception) {
                _deleteArticleState.value = DeleteArticleState(error = e.message ?: "Unknown error")
            }
        }
    }
    fun resetDeleteArticleState() {
        _deleteArticleState.value = DeleteArticleState()
    }
}

data class GetNewsState(
    val isLoading: Boolean = false,
    val getNewsData: List<Article> = emptyList(),
    val error: String? = null
)

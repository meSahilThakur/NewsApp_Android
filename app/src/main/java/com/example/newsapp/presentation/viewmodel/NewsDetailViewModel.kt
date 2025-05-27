package com.example.newsapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.usecase.DeleteArticleUseCase
import com.example.newsapp.domain.usecase.GetArticleDetailUseCase
import com.example.newsapp.domain.usecase.IsArticleSavedUseCase
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
class NewsDetailViewModel @Inject constructor(
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val isArticleSavedUseCase: IsArticleSavedUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,

    private val savedStateHandle: SavedStateHandle      // Inject SavedStateHandle to access navigation arguments
) : ViewModel() {

    private val _articleState = MutableStateFlow(NewsDetailState())
    val articleState = _articleState.asStateFlow()

    private val _isSavedState = MutableStateFlow(IsSavedState())
    val isSavedState = _isSavedState.asStateFlow()

    private val _saveArticleState = MutableStateFlow(SaveArticleState())
    val saveArticleState = _saveArticleState.asStateFlow()

    private val _deleteArticleState = MutableStateFlow(DeleteArticleState())
    val deleteArticleState = _deleteArticleState.asStateFlow()

    private val articleUrl: String? = savedStateHandle["articleUrl"]            // Get the article URL from SavedStateHandle when the ViewModel is created

    init {
        if (articleUrl != null){
            getArticleDetails(articleUrl)                       // Start fetching article details
            checkIfArticleIsSaved(articleUrl)                   // Start checking saved status
        }else{
            _articleState.value = NewsDetailState(error = "Article Url is missing")
            Log.e("NewsDetailViewModel", "Article URL is missing from navigation arguments.")
        }
    }

    fun getArticleDetails(url: String){
        viewModelScope.launch {
            getArticleDetailUseCase(articleUrl = url).collectLatest{
                when(it){
                    is Resource.Loading -> _articleState.value = NewsDetailState(isLoading = true)
                    is Resource.Success -> _articleState.value = NewsDetailState(data = it.data)
                    is Resource.Error -> _articleState.value = NewsDetailState(error = it.error)
                }
            }
        }
    }

    private fun checkIfArticleIsSaved(url: String){
        viewModelScope.launch {
            _isSavedState.value = IsSavedState(isLoading = true)
            try {
                val isSaved = isArticleSavedUseCase(url)
                _isSavedState.value = IsSavedState(data = isSaved)
            }catch (e: Exception){
                _isSavedState.value = IsSavedState(error = e.localizedMessage?: "Getting error to check if article is saved")
            }
        }
    }

    fun toggleSaveStatus(article: Article?){

        if (article == null || article.url == null) {                        // Basic validation: Ensure article object and its URL are available
            Log.e("NewsDetailViewModel", "Cannot toggle save status: Article or URL is null.")
            // Optional: Update a state or trigger an event to show a user-friendly error message in the UI
            return
        }

        viewModelScope.launch {
            _saveArticleState.value = SaveArticleState(isLoading = true)
            _deleteArticleState.value = DeleteArticleState(isLoading = true)

            try {
                val currentlySaved = isArticleSavedUseCase(article?.url!!)
                if (currentlySaved){
                    deleteArticleUseCase(article)
                    _isSavedState.value = IsSavedState(data = false)
                    _deleteArticleState.value = DeleteArticleState(isSuccess = true)
                    _deleteArticleState.value = _deleteArticleState.value.copy(isLoading = false) // Reset loading for delete
                    resetSaveArticleState()
                }else{
                    saveArticleUseCase(article)
                    _isSavedState.value = IsSavedState(data = true)
                    _saveArticleState.value = SaveArticleState(isSuccess = true) // Set success for save
                    _saveArticleState.value = _saveArticleState.value.copy(isLoading = false) // Reset loading for save
                    resetDeleteArticleState() // Reset delete state as it wasn't used for save
                }
            }catch (e: Exception){
                Log.e("NewsDetailViewModel", "Error toggling save status", e)
                if (_isSavedState.value.data == true) { // If delete failed
                    _deleteArticleState.value = DeleteArticleState(isError = true, error = e.localizedMessage)
                    _deleteArticleState.value = _deleteArticleState.value.copy(isLoading = false)
                    resetSaveArticleState()
                } else { // If save failed
                    _saveArticleState.value = SaveArticleState(isError = true, error = e.localizedMessage)
                    _saveArticleState.value = _saveArticleState.value.copy(isLoading = false)
                    resetDeleteArticleState()
                }
            }
        }
    }
    fun resetSaveArticleState() {
        _saveArticleState.value = SaveArticleState()
    }

    fun resetDeleteArticleState() {
        _deleteArticleState.value = DeleteArticleState()
    }
}

data class NewsDetailState(
    val isLoading: Boolean = false,
    val data: Article? = null,
    val error: String = ""
)

data class IsSavedState(
    val isLoading: Boolean = false,
    val data: Boolean = false,
    val error: String = ""
)
